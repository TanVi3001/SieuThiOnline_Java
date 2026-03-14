package business.sql.hr_kpi;

import java.util.ArrayList;
import java.sql.Date;

public class AttendanceSql {
    public static AttendanceSql getInstance() {
        return new AttendanceSql();
    }

    public int insertAttendance(Object t) { return 0; }
    
    // Tìm kiếm dựa trên cặp khóa chính
    public Object selectByCompositeKey(String employeeId, String shiftId, Date workDate) {
        return null;
    }

    public int updateAttendance(Object t) { return 0; }
    
    // Xóa mềm
    public int deleteAttendance(String employeeId, String shiftId) { return 0; }
}