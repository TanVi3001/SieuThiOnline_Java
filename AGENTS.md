# AGENTS.md

## Project overview
- This repository contains a Java desktop application for managing an online supermarket: authentication, sales, inventory, employees, roles, reporting, and statistics.
- Main stack: Java 21, Maven, Swing GUI with NetBeans Form Designer, Oracle JDBC, Apache POI, iText 7, FlatLaf, Log4j, jBCrypt, JavaMail.
- Main entrypoint: `src/main/java/business/main/SieuThiOnline.java` (`business.main.SieuThiOnline`).
- Important directories: `src/main/java` for application code, `src/main/resources` for images/resources, `database` for schema/migration SQL, `target` for Maven build output.

## Repository structure
- `src/main/java/business/main`: application bootstrap and main launcher.
- `src/main/java/business/service`: business logic and orchestration services such as login and payment flows.
- `src/main/java/business/sql`: JDBC-oriented data access grouped by domain (`prod_inventory`, `sales_order`, `rbac`, `promotion`, `hr_kpi`).
- `src/main/java/common`: shared database, report, security, and utility helpers.
- `src/main/java/model`: domain models grouped by area (`account`, `employee`, `order`, `payment`, `product`, `promotion`, `delivery`).
- `src/main/java/view`: Swing screens and panels. Many classes have paired NetBeans `.form` files.
- `src/main/java/view/components`: reusable UI widgets and dashboard/sidebar pieces.
- `src/main/resources/view/image`: bundled UI images/icons.
- `database`: SQL initialization and migration scripts plus ERD/reference diagrams.
- `README.md` and `CONTRIBUTING.md`: project and workflow documentation.
- `pom.xml`: Maven build and dependencies.
- `nbactions.xml`: NetBeans run/debug/profile actions.
- `target`: generated build artifacts. Avoid editing.
- Avoid editing `.form`-managed generated UI sections inside `//GEN-BEGIN` / `//GEN-END` blocks unless the task explicitly requires NetBeans form changes.
- Avoid editing `database/*.sql`, `pom.xml`, `nbactions.xml`, compiled `.class` files, or files under `target` unless the task explicitly calls for it.

## Development commands
- Install/build dependencies: `mvn clean install`
- Compile: `mvn compile`
- Package: `mvn package`
- Run app: `mvn exec:java -Dexec.mainClass="business.main.SieuThiOnline"`
- NetBeans run action equivalent: `mvn process-classes org.codehaus.mojo:exec-maven-plugin:3.5.1:exec`
- Lint: Not found
- Typecheck: Not found
- Test: Not found
- Format: Not found

## Coding principles
- Think before coding: for non-trivial tasks, inspect the affected packages first, state assumptions, and ask when requirements are genuinely ambiguous or risky.
- Simplicity first: implement the smallest change that solves the request; avoid speculative abstractions, broad cleanup, or architecture changes.
- Surgical changes: touch only the files required for the task, preserve package/class structure, reuse existing services/utilities/models, and match the surrounding code style.
- Goal-driven execution: define what success looks like, then verify with the narrowest relevant command such as `mvn compile` or a focused manual check.
- Every changed line should trace back to the task.
- Preserve NetBeans-managed Swing code: do not hand-edit generated component initialization blocks when a constructor, helper method, event handler, or `.form` edit will do.

## Team repo safety rules
- Never rewrite history.
- Never force-push.
- Never change public APIs, database schema, migrations, auth, permissions, billing, or deployment config unless explicitly requested.
- Never include secrets, tokens, credentials, or private keys.
- Do not modify lockfiles unless dependency changes require it.
- Do not edit generated files unless the task specifically requires it.
- Mention unrelated issues in the final response instead of fixing them.
- Do not add new Maven dependencies unless they are necessary for the task and explicitly approved.
- Do not manually edit compiled artifacts such as `AddAdmin.class` or anything inside `target`.

## Verification policy
- Before finishing, run the narrowest relevant verification command.
- Prefer targeted tests over full test suites when possible.
- If modifying types/interfaces, run typecheck if available.
- If modifying formatting/lint-sensitive files, run lint/format checks if available.
- For most Java code changes in this repo, prefer `mvn compile` as the baseline verification command.
- For documentation-only changes, verify the exact diff and render/contents instead of running unrelated builds.
- If verification cannot be run, explain why and list the exact command a human should run.

## Git and commit policy
After completing each task, create a commit automatically only when all of the following are true:
- The user asked for an implementation or file change.
- The task is complete.
- Relevant verification passed, or the user explicitly allowed committing without verification.
- The diff contains only task-related changes.
- There are no unresolved conflicts or suspicious unrelated modifications.

Commit workflow:
1. Run `git status --short`.
2. Review `git diff`.
3. Stage only task-related files with explicit paths, not `git add .`, unless every changed file is clearly part of the task.
4. Use a concise Conventional Commit message when possible:
   - `feat: ...`
   - `fix: ...`
   - `docs: ...`
   - `refactor: ...`
   - `test: ...`
   - `chore: ...`
5. Create the commit.
6. In the final response, include:
   - Commit hash
   - Files changed
   - Verification commands run
   - Any known limitations

Do not commit when:
- Tests/typecheck fail, unless the user explicitly says to commit anyway.
- The work is exploratory.
- The user asked only for analysis, review, planning, or explanation.
- The diff includes unrelated local changes.
- The task requires human review before commit.

## Final response format
Future agents should end coding tasks with:
- Summary
- Verification
- Commit
- Notes / follow-ups, if any
