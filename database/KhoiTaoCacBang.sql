-- ==========================================================
-- 1. ACCOUNT MANAGEMENT AND ROLE-BASED ACCESS CONTROL (RBAC)
-- ==========================================================

CREATE TABLE USERS (
    user_id VARCHAR2(50) PRIMARY KEY,
    full_name NVARCHAR2(255),
    email VARCHAR2(150),
    phone_number VARCHAR2(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE FUNCTIONS (
    function_id VARCHAR2(50) PRIMARY KEY,
    function_name NVARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE ROLE_GROUPS (
    role_group_id VARCHAR2(50) PRIMARY KEY,
    group_name NVARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE ACCOUNTS (
    account_id VARCHAR2(50) PRIMARY KEY,
    user_id VARCHAR2(50) NOT NULL,
    username VARCHAR2(50) NOT NULL,
    password VARCHAR2(255) NOT NULL,
    status NVARCHAR2(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_ACCOUNTS_USERS FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

CREATE TABLE ROLES (
    role_id VARCHAR2(50) PRIMARY KEY,
    function_id VARCHAR2(50) NOT NULL,
    can_view NUMBER(1) DEFAULT 0,
    can_add NUMBER(1) DEFAULT 0,
    can_edit NUMBER(1) DEFAULT 0,
    can_delete NUMBER(1) DEFAULT 0,
    can_export NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_ROLES_FUNCTIONS FOREIGN KEY (function_id) REFERENCES FUNCTIONS(function_id)
);

CREATE TABLE ACCOUNT_ASSIGN_ROLE_GROUP (
    account_id VARCHAR2(50),
    role_group_id VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    PRIMARY KEY (account_id, role_group_id),
    CONSTRAINT FK_AARG_ACCOUNTS FOREIGN KEY (account_id) REFERENCES ACCOUNTS(account_id),
    CONSTRAINT FK_AARG_ROLE_GROUPS FOREIGN KEY (role_group_id) REFERENCES ROLE_GROUPS(role_group_id)
);

CREATE TABLE ROLE_GROUP_ASSIGN_ROLE (
    role_group_id VARCHAR2(50),
    role_id VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    PRIMARY KEY (role_group_id, role_id),
    CONSTRAINT FK_RGAR_ROLE_GROUPS FOREIGN KEY (role_group_id) REFERENCES ROLE_GROUPS(role_group_id),
    CONSTRAINT FK_RGAR_ROLES FOREIGN KEY (role_id) REFERENCES ROLES(role_id)
);

CREATE TABLE ACCOUNT_ASSIGN_ROLE (
    account_id VARCHAR2(50),
    role_id VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    PRIMARY KEY (account_id, role_id),
    CONSTRAINT FK_AAR_ACCOUNTS FOREIGN KEY (account_id) REFERENCES ACCOUNTS(account_id),
    CONSTRAINT FK_AAR_ROLES FOREIGN KEY (role_id) REFERENCES ROLES(role_id)
);

CREATE TABLE TOKENS (
    token_id VARCHAR2(50) PRIMARY KEY,
    account_id VARCHAR2(50) NOT NULL,
    token_value VARCHAR2(500) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_revoked NUMBER(1) DEFAULT 0, -- Trạng thái hủy token (TrangThaiHuy)
    is_deleted NUMBER(1) DEFAULT 0, -- Trạng thái xóa mềm
    CONSTRAINT FK_TOKENS_ACCOUNTS FOREIGN KEY (account_id) REFERENCES ACCOUNTS(account_id)
);
-- ==========================================================
-- 2. HUMAN RESOURCES AND KPI MANAGEMENT
-- ==========================================================

CREATE TABLE SHIFTS (
    shift_id VARCHAR2(50) PRIMARY KEY,
    shift_name NVARCHAR2(50),
    start_time DATE,
    end_time DATE,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE EMPLOYEES (
    employee_id VARCHAR2(50) PRIMARY KEY,
    employee_name NVARCHAR2(100),
    hire_date DATE,
    salary_coefficient NUMBER(5, 2),
    total_completed_orders NUMBER(10) DEFAULT 0,
    role_id VARCHAR2(50),
    shift_id VARCHAR2(50),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_EMPLOYEES_ROLES FOREIGN KEY (role_id) REFERENCES ROLES(role_id),
    CONSTRAINT FK_EMPLOYEES_SHIFTS FOREIGN KEY (shift_id) REFERENCES SHIFTS(shift_id)
);

CREATE TABLE EMPLOYEES (
    employee_id VARCHAR2(50) PRIMARY KEY,
    employee_name NVARCHAR2(100),

    -- giữ cột cũ
    hire_date DATE,
    salary_coefficient NUMBER(5, 2),
    total_completed_orders NUMBER(10) DEFAULT 0,
    role_id VARCHAR2(50),
    shift_id VARCHAR2(50),

    -- thêm cho EmployeeView
    phone VARCHAR2(20),
    email VARCHAR2(100),
    gender NVARCHAR2(10),

    is_deleted NUMBER(1) DEFAULT 0,

    CONSTRAINT FK_EMPLOYEES_ROLES FOREIGN KEY (role_id) REFERENCES ROLES(role_id),
    CONSTRAINT FK_EMPLOYEES_SHIFTS FOREIGN KEY (shift_id) REFERENCES SHIFTS(shift_id)
);

CREATE TABLE KPI_CRITERIA (
    kpi_id VARCHAR2(50) PRIMARY KEY,
    criteria_name NVARCHAR2(100),
    criteria_type NVARCHAR2(50),
    weight NUMBER(3, 2),
    recorded_time TIMESTAMP,
    minimum_target NUMBER(15, 2),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE KPI_EVALUATION (
    employee_id VARCHAR2(50),
    kpi_id VARCHAR2(50),
    evaluation_period VARCHAR2(20),
    actual_value NUMBER(15, 2),
    achieved_score NUMBER(5, 2),
    manager_note NVARCHAR2(255),
    is_deleted NUMBER(1) DEFAULT 0,
    PRIMARY KEY (employee_id, kpi_id, evaluation_period),
    CONSTRAINT FK_EVAL_EMPLOYEES FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id),
    CONSTRAINT FK_EVAL_KPI FOREIGN KEY (kpi_id) REFERENCES KPI_CRITERIA(kpi_id)
);

-- ==========================================================
-- 3. PRODUCTS AND INVENTORY MANAGEMENT
-- ==========================================================

CREATE TABLE CATEGORIES (
    category_id VARCHAR2(50) PRIMARY KEY,
    category_name NVARCHAR2(100) NOT NULL,
    description NVARCHAR2(255),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE SUPPLIERS (
    supplier_id VARCHAR2(50) PRIMARY KEY,
    supplier_name NVARCHAR2(150) NOT NULL,
    email VARCHAR2(100),
    address NVARCHAR2(200),
    phone_number VARCHAR2(20),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE PRODUCTS (
    product_id VARCHAR2(50) PRIMARY KEY,
    product_name NVARCHAR2(150) NOT NULL,
    base_price NUMBER(15, 2),
    category_id VARCHAR2(50),
    supplier_id VARCHAR2(50),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_PRODUCTS_CATEGORIES FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id),
    CONSTRAINT FK_PRODUCTS_SUPPLIERS FOREIGN KEY (supplier_id) REFERENCES SUPPLIERS(supplier_id)
);

CREATE TABLE STORES (
    store_id VARCHAR2(50) PRIMARY KEY,
    email VARCHAR2(100),
    address NVARCHAR2(200),
    phone_number VARCHAR2(20),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE INVENTORY (
    product_id VARCHAR2(50),
    store_id VARCHAR2(50),
    quantity NUMBER DEFAULT 0,
    unit NVARCHAR2(50),
    last_updated DATE,
    is_deleted NUMBER(1) DEFAULT 0,
    PRIMARY KEY (product_id, store_id),
    CONSTRAINT FK_INVENTORY_PRODUCTS FOREIGN KEY (product_id) REFERENCES PRODUCTS(product_id),
    CONSTRAINT FK_INVENTORY_STORES FOREIGN KEY (store_id) REFERENCES STORES(store_id)
);

-- ==========================================================
-- 4. SALES AND ORDER FULFILLMENT
-- ==========================================================

CREATE TABLE CUSTOMERS (
    customer_id    VARCHAR2(50) PRIMARY KEY,
    customer_name  NVARCHAR2(100),
    role_id        VARCHAR2(50),
    phone          VARCHAR2(20),
    email          VARCHAR2(100),
    address        NVARCHAR2(255),
    reward_points  NUMBER(10),
    is_deleted     NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_CUSTOMERS_ROLES FOREIGN KEY (role_id) REFERENCES ROLES(role_id)
);

CREATE TABLE PAYMENT_METHODS (
    payment_method_id VARCHAR2(50) PRIMARY KEY,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE CASH_PAYMENT (
    payment_method_id VARCHAR2(50) PRIMARY KEY,
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_CASH_PM FOREIGN KEY (payment_method_id) REFERENCES PAYMENT_METHODS(payment_method_id)
);

CREATE TABLE BANK_TRANSFER_PAYMENT (
    payment_method_id VARCHAR2(50) PRIMARY KEY,
    bank_name NVARCHAR2(100),
    transaction_time TIMESTAMP,
    sender_account_number VARCHAR2(50),
    qr_code VARCHAR2(255),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_BT_PM FOREIGN KEY (payment_method_id) REFERENCES PAYMENT_METHODS(payment_method_id)
);

CREATE TABLE ORDERS
(
    order_id          VARCHAR2(50) PRIMARY KEY,
    customer_id       VARCHAR2(50),
    payment_method_id VARCHAR2(50),
    order_date        DATE,
    status            NVARCHAR2(50),
    total_amount      NUMBER(15, 2),
    note              NVARCHAR2(255),
    employee_id       VARCHAR2(50),
    is_deleted        NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_ORDERS_CUSTOMERS FOREIGN KEY (customer_id) REFERENCES CUSTOMERS (customer_id),
    CONSTRAINT FK_ORDERS_PM FOREIGN KEY (payment_method_id) REFERENCES PAYMENT_METHODS (payment_method_id),
    CONSTRAINT FK_ORDERS_EMPLOYEES FOREIGN KEY (employee_id) REFERENCES EMPLOYEES (employee_id)
);

CREATE TABLE ORDER_DETAILS (
    order_detail_id VARCHAR2(50) PRIMARY KEY,
    order_id VARCHAR2(50),
    product_id VARCHAR2(50),
    quantity NUMBER(10),
    unit_price NUMBER(15, 2),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_OD_ORDERS FOREIGN KEY (order_id) REFERENCES ORDERS(order_id),
    CONSTRAINT FK_OD_PRODUCTS FOREIGN KEY (product_id) REFERENCES PRODUCTS(product_id)
);

CREATE TABLE DELIVERY_MANAGEMENT (
    delivery_id VARCHAR2(50) PRIMARY KEY,
    order_id VARCHAR2(50),
    employee_id VARCHAR2(50),
    execution_date DATE,
    status NVARCHAR2(50),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_DM_ORDERS FOREIGN KEY (order_id) REFERENCES ORDERS(order_id),
    CONSTRAINT FK_DM_EMPLOYEES FOREIGN KEY (employee_id) REFERENCES EMPLOYEES(employee_id)
);

CREATE TABLE ON_SITE_PICKUP (
    delivery_id VARCHAR2(50) PRIMARY KEY,
    counter_position NVARCHAR2(50),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_OSP_DM FOREIGN KEY (delivery_id) REFERENCES DELIVERY_MANAGEMENT(delivery_id)
);

CREATE TABLE STORE_PICKUP (
    delivery_id VARCHAR2(50) PRIMARY KEY,
    locker_id VARCHAR2(50),
    pickup_appointment TIMESTAMP,
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_SP_DM FOREIGN KEY (delivery_id) REFERENCES DELIVERY_MANAGEMENT(delivery_id)
);

CREATE TABLE HOME_DELIVERY (
    delivery_id VARCHAR2(50) PRIMARY KEY,
    delivery_address NVARCHAR2(200),
    shipping_fee NUMBER(15, 2),
    recipient_phone VARCHAR2(20),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_HD_DM FOREIGN KEY (delivery_id) REFERENCES DELIVERY_MANAGEMENT(delivery_id)
);

-- ==========================================================
-- 5. PROMOTION CAMPAIGNS
-- ==========================================================

CREATE TABLE PROMOTION_CAMPAIGNS (
    campaign_id VARCHAR2(50) PRIMARY KEY,
    campaign_name NVARCHAR2(150) NOT NULL,
    description NVARCHAR2(255),
    start_date DATE,
    end_date DATE,
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE TABLE PROMOTIONS (
    promotion_id VARCHAR2(50) PRIMARY KEY,
    promotion_name NVARCHAR2(150) NOT NULL,
    campaign_id VARCHAR2(50),
    application_condition NVARCHAR2(255),
    status NVARCHAR2(50),
    order_detail_id VARCHAR2(50),
    discount_amount NUMBER(15, 2),
    is_deleted NUMBER(1) DEFAULT 0,
    CONSTRAINT FK_PROMOTIONS_CAMPAIGNS FOREIGN KEY (campaign_id) REFERENCES PROMOTION_CAMPAIGNS(campaign_id),
    CONSTRAINT FK_PROMOTIONS_OD FOREIGN KEY (order_detail_id) REFERENCES ORDER_DETAILS(order_detail_id)
);
