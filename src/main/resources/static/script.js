
const API = {
    students: "/api/students",
    subjects: "/api/subjects",
    records: "/api/academic-records"
};

// Application data
let students = [];
let subjects = [];
let records = [];

// HTML elements
const studentForm = document.getElementById("studentForm");
const recordForm = document.getElementById("recordForm");

const studentTableBody = document.getElementById("studentTableBody");
const subjectTableBody = document.getElementById("subjectTableBody");
const recordTableBody = document.getElementById("recordTableBody");

// ==========================================
// 1. FETCH DATA
// ==========================================

async function fetchData(url) {
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error(`Request failed: ${response.status}`);
    }

    return response.json();
}

// ==========================================
// 2. HTML ESCAPING
// ==========================================

function escapeHTML(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[character]);
}

// ==========================================
// 3. LOAD APPLICATION DATA
// ==========================================

async function loadDashboard() {
    try {
        const results = await Promise.all([
            fetchData(API.students),
            fetchData(API.subjects),
            fetchData(API.records)
        ]);

        students = results[0];
        subjects = results[1];
        records = results[2];

        updateStatistics();
        renderDashboardStudents();
        renderStudents();
        renderSubjects();
        renderRecords();
        populateRecordDropdowns();

    } catch (error) {
        console.error("Dashboard loading error:", error);

        studentTableBody.innerHTML =
            '<tr><td colspan="9">Unable to load students. Check whether the backend is running.</td></tr>';

        subjectTableBody.innerHTML =
            '<tr><td colspan="6">Unable to load subjects.</td></tr>';

        recordTableBody.innerHTML =
            '<tr><td colspan="6">Unable to load academic records.</td></tr>';
    }
}

// ==========================================
// 4. DASHBOARD STATISTICS
// ==========================================

function updateStatistics() {
    document.getElementById("totalStudents").textContent = students.length;
    document.getElementById("totalSubjects").textContent = subjects.length;
    document.getElementById("totalRecords").textContent = records.length;

    const branches = new Set(students.map(student => student.branch));
    document.getElementById("totalBranches").textContent = branches.size;
}

// ==========================================
// 5. DASHBOARD STUDENT PREVIEW
// ==========================================

function renderDashboardStudents() {
    const body = document.getElementById("dashboardStudentsBody");

    if (!body) return;

    const recentStudents = students.slice(0, 5);

    if (recentStudents.length === 0) {
        body.innerHTML = '<tr><td colspan="5">No students found.</td></tr>';
        return;
    }

    body.innerHTML = recentStudents.map(student => `
        <tr>
            <td>${student.id}</td>
            <td>${escapeHTML(student.registrationNumber)}</td>
            <td>${escapeHTML(student.name)}</td>
            <td>${escapeHTML(student.branch)}</td>
            <td>${student.semester}</td>
        </tr>
    `).join("");
}

// ==========================================
// 6. DISPLAY STUDENTS
// ==========================================

function renderStudents() {
    const search = document.getElementById("searchInput").value.toLowerCase();
    const branch = document.getElementById("branchFilter").value;
    const year = document.getElementById("yearFilter").value;
    const semester = document.getElementById("semesterFilter").value;

    const filteredStudents = students.filter(student => {
        const matchesSearch =
            String(student.name).toLowerCase().includes(search) ||
            String(student.registrationNumber).toLowerCase().includes(search);

        const matchesBranch = !branch || student.branch === branch;
        const matchesYear = !year || Number(student.studyYear) === Number(year);
        const matchesSemester =
            !semester || Number(student.semester) === Number(semester);

        return matchesSearch && matchesBranch && matchesYear && matchesSemester;
    });

    if (filteredStudents.length === 0) {
        studentTableBody.innerHTML =
            '<tr><td colspan="9">No matching students found.</td></tr>';
        return;
    }

    studentTableBody.innerHTML = filteredStudents.map(student => `
        <tr>
            <td>${student.id}</td>
            <td>${escapeHTML(student.registrationNumber)}</td>
            <td>${escapeHTML(student.name)}</td>
            <td>${escapeHTML(student.branch)}</td>
            <td>${student.admissionYear}</td>
            <td>${student.studyYear}</td>
            <td>${student.semester}</td>
            <td>${escapeHTML(student.section)}</td>
            <td>
                <button onclick="viewStudentProfile(${Number(student.id)})">
                    Profile
                </button>

                <button onclick="editStudent(${Number(student.id)})">
                    Edit
                </button>

                <button class="delete-button"
                        onclick="deleteStudent(${Number(student.id)})">
                    Delete
                </button>
            </td>
        </tr>
    `).join("");
}

// ==========================================
// 7. DISPLAY SUBJECTS
// ==========================================

function renderSubjects() {
    if (subjects.length === 0) {
        subjectTableBody.innerHTML =
            '<tr><td colspan="6">No subjects found.</td></tr>';
        return;
    }

    subjectTableBody.innerHTML = subjects.map(subject => `
        <tr>
            <td>${escapeHTML(subject.subjectCode)}</td>
            <td>${escapeHTML(subject.subjectName)}</td>
            <td>${escapeHTML(subject.branch)}</td>
            <td>${subject.semester}</td>
            <td>${subject.maxInternalMarks}</td>
            <td>${subject.maxExternalMarks}</td>
        </tr>
    `).join("");
}

// ==========================================
// 8. DISPLAY ACADEMIC RECORDS
// ==========================================

function renderRecords() {
    const studentGroups = new Map();

    records.forEach(record => {
        const student = record.student;

        if (!student || student.id == null) return;

        if (!studentGroups.has(String(student.id))) {
            studentGroups.set(String(student.id), {
                student,
                records: []
            });
        }

        studentGroups.get(String(student.id)).records.push(record);
    });

    const groupedStudents = Array.from(studentGroups.values());

    if (groupedStudents.length === 0) {
        recordTableBody.innerHTML =
            '<tr><td colspan="6">No academic records found.</td></tr>';
        return;
    }

    recordTableBody.innerHTML = groupedStudents.map(group => {
        const student = group.student;

        return `
            <tr>
                <td>${escapeHTML(student.name)}</td>
                <td>${escapeHTML(student.registrationNumber)}</td>
                <td>${escapeHTML(student.branch)}</td>
                <td>${student.studyYear}</td>
                <td>${group.records.length}</td>
                <td>
                    <button onclick="viewStudentProfile(${Number(student.id)})">
                        View Complete Profile
                    </button>
                </td>
            </tr>
        `;
    }).join("");
}

// ==========================================
// 9. ADD AND UPDATE STUDENTS
// ==========================================

studentForm.addEventListener("submit", async event => {
    event.preventDefault();

    const id = document.getElementById("studentId").value;

    const student = {
        registrationNumber:
            document.getElementById("registrationNumber").value.trim(),

        name:
            document.getElementById("name").value.trim(),

        branch:
            document.getElementById("branch").value,

        admissionYear:
            Number(document.getElementById("admissionYear").value),

        studyYear:
            Number(document.getElementById("studyYear").value),

        semester:
            Number(document.getElementById("semester").value),

        section:
            document.getElementById("section").value
    };

    try {
        const response = await fetch(
            id ? `${API.students}/${id}` : API.students,
            {
                method: id ? "PUT" : "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(student)
            }
        );

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert(id ? "Student updated successfully!" : "Student added successfully!");

        studentForm.reset();
        resetForm();

        await loadDashboard();

    } catch (error) {
        console.error(error);
        alert("Unable to save student: " + error.message);
    }
});

// ==========================================
// 10. EDIT STUDENT
// ==========================================

function editStudent(id) {
    const student = students.find(
        item => String(item.id) === String(id)
    );

    if (!student) {
        alert("Student not found.");
        return;
    }

    document.getElementById("studentId").value = student.id;
    document.getElementById("registrationNumber").value = student.registrationNumber;
    document.getElementById("name").value = student.name;
    document.getElementById("branch").value = student.branch;
    document.getElementById("admissionYear").value = student.admissionYear;
    document.getElementById("studyYear").value = student.studyYear;
    document.getElementById("semester").value = student.semester;
    document.getElementById("section").value = student.section;

    document.getElementById("formTitle").textContent = "Edit Student";
    document.getElementById("submitButton").textContent = "Update Student";
    document.getElementById("cancelButton").hidden = false;

    studentForm.scrollIntoView({ behavior: "smooth" });
}

// ==========================================
// 11. DELETE STUDENT
// ==========================================

async function deleteStudent(id) {
    const confirmed = confirm("Are you sure you want to delete this student?");

    if (!confirmed) return;

    try {
        const response = await fetch(`${API.students}/${id}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert("Student deleted successfully!");
        await loadDashboard();

    } catch (error) {
        console.error(error);
        alert("Unable to delete student: " + error.message);
    }
}

// ==========================================
// 12. RESET STUDENT FORM
// ==========================================

function resetForm() {
    document.getElementById("studentId").value = "";
    document.getElementById("formTitle").textContent = "Add New Student";
    document.getElementById("submitButton").textContent = "Add Student";
    document.getElementById("cancelButton").hidden = true;
}

document.getElementById("cancelButton").addEventListener("click", () => {
    studentForm.reset();
    resetForm();
});

// ==========================================
// 13. STUDENT SEARCH AND FILTERS
// ==========================================

document.getElementById("searchInput").addEventListener("input", renderStudents);
document.getElementById("branchFilter").addEventListener("change", renderStudents);
document.getElementById("yearFilter").addEventListener("change", renderStudents);
document.getElementById("semesterFilter").addEventListener("change", renderStudents);

// ==========================================
// 14. STUDENT PROFILE
// ==========================================

async function viewStudentProfile(studentId) {
    const student = students.find(
        item => String(item.id) === String(studentId)
    );

    if (!student) {
        alert("Student not found.");
        return;
    }

    // Open the dedicated profile page, not the Students directory.
    showView("profileView");

    const studentProfile = document.getElementById("studentProfile");
    const profileRecordsBody = document.getElementById("profileRecordsBody");
    const attendanceRecordsBody = document.getElementById("attendanceRecordsBody");

    studentProfile.innerHTML = "<p>Loading student profile...</p>";

    profileRecordsBody.innerHTML =
        '<tr><td colspan="5">Loading subject records...</td></tr>';

    attendanceRecordsBody.innerHTML =
        '<tr><td colspan="4">Loading attendance records...</td></tr>';

    showProfileTab("overviewPanel");

    try {
        const studentRecords = await fetchData(
            `${API.records}/student/${student.id}`
        );

        let totalMarks = 0;
        let maximumMarks = 0;
        let totalAttended = 0;
        let totalConducted = 0;

        studentRecords.forEach(record => {
            const subject = record.subject || {};

            totalMarks += Number(record.totalMarks || 0);

            // Correct maximum marks property names.
            maximumMarks +=
                Number(subject.maxInternalMarks || 0) +
                Number(subject.maxExternalMarks || 0);

            totalAttended += Number(record.classesAttended || 0);
            totalConducted += Number(record.classesConducted || 0);
        });

        const averagePercentage = maximumMarks > 0
            ? ((totalMarks / maximumMarks) * 100).toFixed(2)
            : "0.00";

        const attendancePercentage = totalConducted > 0
            ? ((totalAttended / totalConducted) * 100).toFixed(2)
            : "0.00";

        // OVERVIEW
        studentProfile.innerHTML = `
            <div class="profile-grid">

                <div class="profile-card">
                    <h3>${escapeHTML(student.name)}</h3>

                    <p><strong>Registration Number:</strong>
                        ${escapeHTML(student.registrationNumber)}</p>

                    <p><strong>Branch:</strong>
                        ${escapeHTML(student.branch)}</p>

                    <p><strong>Admission Year:</strong>
                        ${student.admissionYear}</p>

                    <p><strong>Study Year:</strong>
                        ${student.studyYear}</p>

                    <p><strong>Semester:</strong>
                        ${student.semester}</p>

                    <p><strong>Section:</strong>
                        ${escapeHTML(student.section)}</p>
                </div>

                <div class="info-card">
                    <h3>Subjects</h3>
                    <p>${studentRecords.length}</p>
                </div>

                <div class="info-card">
                    <h3>Marks Percentage</h3>
                    <p>${averagePercentage}%</p>
                </div>

                <div class="info-card">
                    <h3>Overall Attendance</h3>
                    <p>${attendancePercentage}%</p>
                    <small>${totalAttended} / ${totalConducted} classes</small>
                </div>

            </div>
        `;

        if (studentRecords.length === 0) {
            profileRecordsBody.innerHTML =
                '<tr><td colspan="5">No subject records found.</td></tr>';

            attendanceRecordsBody.innerHTML =
                '<tr><td colspan="4">No attendance records found.</td></tr>';

            return;
        }

        // SUBJECTS AND MARKS
        profileRecordsBody.innerHTML = studentRecords.map(record => {
            const subject = record.subject || {};

            return `
                <tr>
                    <td>${escapeHTML(subject.subjectName || "N/A")}</td>
                    <td>${escapeHTML(record.academicYear || "N/A")}</td>
                    <td>${record.internalMarks ?? "N/A"}</td>
                    <td>${record.externalMarks ?? "N/A"}</td>
                    <td>${record.totalMarks ?? "N/A"}</td>
                </tr>
            `;
        }).join("");

        // ATTENDANCE
        attendanceRecordsBody.innerHTML = studentRecords.map(record => {
            const subject = record.subject || {};

            const conducted = Number(record.classesConducted || 0);
            const attended = Number(record.classesAttended || 0);

            const percentage = conducted > 0
                ? ((attended / conducted) * 100).toFixed(2)
                : "0.00";

            return `
                <tr>
                    <td>${escapeHTML(subject.subjectName || "N/A")}</td>
                    <td>${conducted}</td>
                    <td>${attended}</td>
                    <td>${percentage}%</td>
                </tr>
            `;
        }).join("");

    } catch (error) {
        console.error("Student profile error:", error);

        studentProfile.innerHTML =
            "<p>Unable to load student profile. Please try again.</p>";

        profileRecordsBody.innerHTML =
            '<tr><td colspan="5">Unable to load subject records.</td></tr>';

        attendanceRecordsBody.innerHTML =
            '<tr><td colspan="4">Unable to load attendance records.</td></tr>';
    }
}

// ==========================================
// 15. PROFILE TABS
// ==========================================

function showProfileTab(panelId) {
    document.querySelectorAll(".profile-panel").forEach(panel => {
        panel.hidden = panel.id !== panelId;
    });

    document.querySelectorAll(".profile-tab").forEach(tab => {
        tab.classList.toggle(
            "active",
            tab.dataset.profileTab === panelId
        );
    });
}

document.querySelectorAll(".profile-tab").forEach(tab => {
    tab.addEventListener("click", () => {
        showProfileTab(tab.dataset.profileTab);
    });
});

// Back to Students from the profile page.
document.getElementById("closeProfileButton").addEventListener("click", () => {
    showView("studentsView");
});

// ==========================================
// 16. POPULATE ACADEMIC RECORD DROPDOWNS
// ==========================================

function populateRecordDropdowns() {
    const studentSelect = document.getElementById("recordStudent");
    const subjectSelect = document.getElementById("recordSubject");

    studentSelect.innerHTML = '<option value="">Select student</option>';

    students.forEach(student => {
        studentSelect.innerHTML += `
            <option value="${student.id}">
                ${escapeHTML(student.registrationNumber)} -
                ${escapeHTML(student.name)}
            </option>
        `;
    });

    subjectSelect.innerHTML = '<option value="">Select subject</option>';

    subjects.forEach(subject => {
        subjectSelect.innerHTML += `
            <option value="${subject.id}">
                ${escapeHTML(subject.subjectCode)} -
                ${escapeHTML(subject.subjectName)}
            </option>
        `;
    });
}

// ==========================================
// 17. ADD ACADEMIC RECORD
// ==========================================

recordForm.addEventListener("submit", async event => {
    event.preventDefault();

    const studentId = Number(document.getElementById("recordStudent").value);
    const subjectId = Number(document.getElementById("recordSubject").value);
    const internalMarks = Number(document.getElementById("internalMarks").value);
    const externalMarks = Number(document.getElementById("externalMarks").value);
    const classesConducted = Number(document.getElementById("classesConducted").value);
    const classesAttended = Number(document.getElementById("classesAttended").value);

    const selectedSubject = subjects.find(
        subject => Number(subject.id) === subjectId
    );

    if (!selectedSubject) {
        alert("Please select a valid subject.");
        return;
    }

    if (
        internalMarks < 0 ||
        internalMarks > selectedSubject.maxInternalMarks
    ) {
        alert(
            `Internal marks must be between 0 and ${selectedSubject.maxInternalMarks}.`
        );
        return;
    }

    if (
        externalMarks < 0 ||
        externalMarks > selectedSubject.maxExternalMarks
    ) {
        alert(
            `External marks must be between 0 and ${selectedSubject.maxExternalMarks}.`
        );
        return;
    }

    if (classesConducted < 0 || classesAttended < 0) {
        alert("Attendance values cannot be negative.");
        return;
    }

    if (classesAttended > classesConducted) {
        alert("Classes attended cannot exceed classes conducted.");
        return;
    }

    const record = {
        student: { id: studentId },
        subject: { id: subjectId },
        academicYear: Number(document.getElementById("academicYear").value),
        internalMarks,
        externalMarks,
        classesConducted,
        classesAttended
    };

    try {
        const response = await fetch(API.records, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(record)
        });

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert("Academic record added successfully!");

        recordForm.reset();
        await loadDashboard();

    } catch (error) {
        console.error("Error adding academic record:", error);
        alert("Unable to add academic record: " + error.message);
    }
});

// ==========================================
// 18. SIDEBAR NAVIGATION
// ==========================================

const navigationButtons = document.querySelectorAll("[data-view]");
const pageViews = document.querySelectorAll(".page-view");
const pageTitle = document.getElementById("pageTitle");
const sidebar = document.getElementById("sidebar");
const sidebarToggle = document.getElementById("sidebarToggle");

function showView(viewId) {
    pageViews.forEach(view => {
        view.hidden = true;
    });

    const selectedView = document.getElementById(viewId);

    if (!selectedView) {
        console.error("Page view not found:", viewId);
        return;
    }

    selectedView.hidden = false;

    document.querySelectorAll(".nav-item").forEach(button => {
        button.classList.remove("active");
    });

    const activeButton = document.querySelector(
        `.nav-item[data-view="${viewId}"]`
    );

    if (activeButton) {
        activeButton.classList.add("active");
    }

    const titles = {
        dashboardView: "Dashboard",
        studentsView: "Students",
        profileView: "Student Profile",
        subjectsView: "Subjects",
        marksView: "Marks & Attendance",
        recordsView: "Academic Records"
    };

    pageTitle.textContent = titles[viewId] || "Dashboard";

    sidebar.classList.remove("open");

    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

navigationButtons.forEach(button => {
    button.addEventListener("click", () => {
        showView(button.dataset.view);
    });
});

sidebarToggle.addEventListener("click", () => {
    sidebar.classList.toggle("open");
});

// ==========================================
// 19. START APPLICATION
// ==========================================

showView("dashboardView");
loadDashboard();
