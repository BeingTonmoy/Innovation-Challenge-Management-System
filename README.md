# Innovation Management System (IMS)

A Java Swing desktop application for managing innovation ideas, departments, evaluations, and accepted projects with an Oracle Database backend.

## Features

- Oracle JDBC database connectivity with multiple local XE and ORCL URL candidates.
- User registration and login with SHA-256 password hashing.
- Role-aware navigation for administrators and innovators.
- Department management for administrators.
- Idea submission with department, category, description, and optional attachment.
- Admin review with score, comments, and APPROVED or REJECTED decisions.
- Project creation from an approved idea.
- Project status, title, description, and date management.
- Project member management.
- Administrator project management, including editing, viewing members, and deletion.
- Compatibility checks for older database schemas.
- PL/SQL procedures for registration and project creation with Java/JDBC fallback.

## Project Structures

| File | Responsibility |
| --- | --- |
| `Start.java` | Application entry point. |
| `Home.java` | Initial home screen. |
| `Login.java` | User authentication. |
| `Registration.java` | Innovator registration. |
| `UserHome.java` | Innovator dashboard. |
| `SubmitIdea.java` | Idea submission and attachments. |
| `Projects.java` | Innovator project creation and member management. |
| `Admin.java` | Administrator dashboard. |
| `InnovationCall.java` | Admin idea review and approval. |
| `AdminProject.java` | Admin project management. |
| `Departments.java` | Department CRUD operations. |
| `Users.java` | User administration. |
| `DBConnection.java` | Oracle connections, compatibility setup, sequences, and PL/SQL procedures. |
| `AdminPassword.java` | Admin password-related screen. |
| `Confirmation.java` | Confirmation screen. |
| `DescribeTable.java` | Database table inspection utility. |
| `DescribeConstraint.java` | Database constraint inspection utility. |
| `ListTables.java` | Database table listing utility. |
| `TestRegistration.java` | Registration database test utility. |
| `images/` | Application images and icons. |
| `lib/` | External libraries, including the Oracle JDBC driver. |
| `bin/` | Optional compiled output directory. |

## Requirements

- Windows, Linux, or macOS.
- Java Development Kit (JDK) 8 or newer.
- Oracle Database XE or ORCL running locally.
- Oracle JDBC driver, normally `ojdbc8.jar`, in `lib/`.
- An Oracle account with permission to create or alter the application tables and procedures.

The default connection settings are:

- Username: `scott`
- Password: `tiger`
- Database URLs: local XE and ORCL service/SID variants on port `1521`

The username and password can be overridden without changing source code:

```powershell
java -Doracle.user=your_user -Doracle.password=your_password -cp ".;lib\ojdbc8.jar" Start
```

## Database Setup

The complete initial Oracle schema is provided in [IMS_SCHEMA.sql](IMS_SCHEMA.sql). Run it once as the application owner in SQL*Plus, SQLcl, or Oracle SQL Developer before starting the application:

```sql
@IMS_SCHEMA.sql
```

`DBConnection.getConnection()` attempts the configured Oracle URLs in order. After connecting, it performs compatibility setup:

1. Ensures the administrator table exists.
2. Ensures the ten entity sequences (`DEPT_SEQ`, `USER_SEQ`, `INNOVATOR_SEQ`, `ADMIN_SEQ`, `CALL_SEQ`, `IDEA_SEQ`, `EVAL_SEQ`, `ATTACH_SEQ`, `PROJECT_SEQ`, and `MEMBER_SEQ`) exist and are advanced past existing IDs when possible.
3. Creates or updates the PL/SQL procedures described below when the account has permission.
4. Initializes the default administrator record when possible.

The application also creates or upgrades project-related tables from `Projects.java` and performs column and constraint checks before using older schemas.

The application expects these main tables, either pre-created or created/upgraded by the application:

- `USER_ACCOUNT`
- `INNOVATOR`
- `ADMIN_USER`
- `DEPARTMENT`
- `IDEA`
- `INNOVATION_CALL`
- `EVALUATION`
- `ATTACHMENT`
- `INNOVATION_PROJECT`
- `PROJECT_MEMBER`

Existing schemas may contain additional columns. The application uses metadata checks for optional columns such as `IDEA.DEPTID`, `PROJECT_MEMBER.JOINDATE`, and project description/status fields.

The updated schema uses `PROJECT_MEMBER.JOINEDDATE` and includes `INNOVATION_CALL.ADMINID`. The application uses `JOINEDDATE` for the current schema and retains compatibility handling for older project-member layouts.

## Compile and Run

From the project directory:

```powershell
cd i:\Java
javac -cp ".;lib\ojdbc8.jar" *.java
java -cp ".;lib\ojdbc8.jar" Start
```

If the JDBC jar has a different filename, replace `ojdbc8.jar` with the actual filename in `lib/`.

To place compiled files in `bin/`:

```powershell
javac -cp "lib\ojdbc8.jar" -d bin *.java
java -cp "bin;lib\ojdbc8.jar" Start
```

## Application Workflow

### Innovator workflow

1. Register an account from the registration screen.
2. Log in with the registered username and password.
3. Open the innovation submission screen.
4. Select a department and category, enter the idea details, and optionally attach a file.
5. Submit the idea. New ideas are stored with status `PENDING`.
6. Open Projects after an administrator approves the idea.
7. Select the approved idea from the dropdown.
8. Enter project dates and optional description, then create the project.
9. Add or remove project members as needed.

### Administrator workflow

1. Log in with an administrator account.
2. Open Idea Proposal to review pending ideas.
3. Enter a score from 1 to 10 and optional comments.
4. Approve or reject the selected idea.
5. Open Project Manage to view and edit accepted project records.
6. Update project title, status, dates, description, or members, or delete a project.
7. Use Departments and Users to maintain application data.

An approved idea must belong to the logged-in innovator for it to appear in that innovator's Projects dropdown. The dropdown matches usernames case-insensitively and accepts both `APPROVED` and `ACCEPTED` status values.

## PL/SQL Integration

`DBConnection.java` creates these procedures when possible:

### `IMS_INIT_APP`

Initializes the default administrator record if it does not already exist.

### `IMS_REGISTER_USER`

Creates a `USER_ACCOUNT` record and its related `INNOVATOR` record in one database operation. It returns the generated user and innovator IDs through OUT parameters.

### `IMS_CREATE_PROJECT`

Creates an `INNOVATION_PROJECT` record and returns the generated project ID through an OUT parameter.

Registration and project creation call these procedures through JDBC `CallableStatement`. If a procedure is unavailable because of missing privileges, an older schema, or a database deployment issue, the application falls back to the original Java/JDBC inserts so the normal workflow can continue.

## Database Transactions

Multi-step operations use transactions:

- Registration creates the account and innovator record in one transaction.
- Idea submission creates the idea and optional attachment in one transaction.
- Admin evaluation creates the evaluation and updates the idea status in one transaction.

An error should leave the transaction uncommitted. When troubleshooting database errors, check the Oracle account privileges and the exact schema column names first.

## Troubleshooting

### Oracle JDBC driver not found

Confirm that the Oracle JDBC jar exists in `lib/` and is included in both the compile and runtime classpaths.

### Unable to connect to Oracle

Check that:

- The Oracle listener and database service are running.
- The configured port is `1521`.
- The selected account and password are correct.
- The account can connect to XE or ORCL.
- The connection URL matches the installed Oracle service or SID.

### Approved idea does not appear in Projects

Confirm that:

- The idea status is `APPROVED` or `ACCEPTED`.
- `IDEA.INNOVATORID` points to the logged-in user's `INNOVATOR` record.
- The username passed from Login to UserHome matches `USER_ACCOUNT.USERNAME`.
- The Projects screen has been refreshed or reopened after approval.

### PL/SQL procedure errors

The procedures require permission to create procedures and to write to the application tables. The Java fallback is intentional. Check the Oracle error message and privileges if you need PL/SQL execution to be mandatory.

### Schema mismatch errors

Oracle identifiers and table columns must match the application queries. Use `DescribeTable.java`, `DescribeConstraint.java`, and `ListTables.java` to inspect the connected schema.

## Cleaning Generated Files

Compiled Java files are generated artifacts and should not be committed to source control. To remove root-level class files in PowerShell:

```powershell
Get-ChildItem -File -Filter *.class | Remove-Item -Force
```

## Development Notes

- Keep database changes compatible with existing Oracle installations.
- Prefer prepared statements for values supplied by users.
- Preserve transaction boundaries for operations that write to multiple tables.
- Test against a live Oracle schema after changing SQL, procedures, or table compatibility logic.
- The project currently uses Swing and JDBC directly rather than a separate framework.
