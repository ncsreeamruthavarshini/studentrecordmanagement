
package com.studentmanagement;

import com.studentmanagement.model.Student;
import com.studentmanagement.model.Subject;
import com.studentmanagement.model.AcademicRecord;

import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.SubjectRepository;
import com.studentmanagement.repository.AcademicRecordRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DemoDataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final AcademicRecordRepository recordRepository;

    private final Random random = new Random(42);

    public DemoDataLoader(
            StudentRepository studentRepository,
            SubjectRepository subjectRepository,
            AcademicRecordRepository recordRepository) {
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public void run(String... args) {

        if (studentRepository.count() > 0 ||
            subjectRepository.count() > 0 ||
            recordRepository.count() > 0) {
            System.out.println("Demo data skipped: database is not empty.");
            return;
        }

        System.out.println("Generating fictional university demo data...");

        String[] branches = {
            "CSE", "CSE (Data Science)", "CSE (AI & ML)",
            "Information Technology", "ECE", "Mechanical Engineering"
        };

        String[] firstNames = {
            "Aarav", "Aditi", "Ananya", "Arjun", "Diya",
            "Ishaan", "Kavya", "Meera", "Neha", "Rohan",
            "Saanvi", "Vihaan", "Aditya", "Priya", "Rahul",
            "Ira", "Kabir", "Nisha", "Reyansh", "Tara"
        };

        String[] lastNames = {
            "Sharma", "Verma", "Mehta", "Kapoor", "Reddy",
            "Nair", "Iyer", "Gupta", "Singh", "Patel",
            "Joshi", "Rao", "Malhotra", "Das", "Menon"
        };

        String[] sections = {"A", "B", "C", "D"};

        String[][] subjectNames = {
            {
                "Programming Fundamentals",
                "Mathematics for Computing",
                "Computer Organization",
                "Data Structures",
                "Digital Logic"
            },
            {
                "Python Programming",
                "Statistics for Data Science",
                "Database Management",
                "Machine Learning",
                "Data Visualization"
            },
            {
                "Artificial Intelligence",
                "Deep Learning",
                "Operating Systems",
                "Computer Networks",
                "Software Engineering"
            },
            {
                "Advanced Algorithms",
                "Cloud Computing",
                "Big Data Analytics",
                "Cyber Security",
                "Project Development"
            }
        };

        String[][] subjectCodes = {
            {"PF", "MATH", "CO", "DS", "DL"},
            {"PY", "STAT", "DB", "ML", "DV"},
            {"AI", "DL", "OS", "CN", "SE"},
            {"AA", "CC", "BDA", "CS", "PD"}
        };

        // Generate 250 students.
        List<Student> students = new ArrayList<>();

        for (int i = 1; i <= 250; i++) {

            int studyYear = ((i - 1) / branches.length) % 4 + 1;
            int semester = studyYear * 2 - 1;
            int admissionYear = 2026 - studyYear + 1;

            String branch = branches[(i - 1) % branches.length];

            String name =
                firstNames[random.nextInt(firstNames.length)] + " " +
                lastNames[random.nextInt(lastNames.length)];

            String registrationNumber =
                String.format("DEMO-%d-%04d", admissionYear, i);

            students.add(new Student(
                registrationNumber,
                name,
                branch,
                admissionYear,
                studyYear,
                semester,
                sections[(i - 1) % sections.length]
            ));
        }

        students = studentRepository.saveAll(students);
        System.out.println("Created " + students.size() + " students.");

        // Generate 120 subjects.
        List<Subject> subjects = new ArrayList<>();

        for (int branchIndex = 0; branchIndex < branches.length; branchIndex++) {

            for (int yearIndex = 0; yearIndex < 4; yearIndex++) {

                int semester = (yearIndex + 1) * 2 - 1;

                for (int subjectIndex = 0; subjectIndex < 5; subjectIndex++) {

                    String code = String.format(
                        "DEMO-%02d-S%d-%s",
                        branchIndex + 1,
                        semester,
                        subjectCodes[yearIndex][subjectIndex]
                    );

                    Subject subject = new Subject();
                    subject.setSubjectCode(code);
                    subject.setSubjectName(subjectNames[yearIndex][subjectIndex]);
                    subject.setBranch(branches[branchIndex]);
                    subject.setSemester(semester);
                    subject.setMaxInternalMarks(30);
                    subject.setMaxExternalMarks(70);

                    subjects.add(subject);
                }
            }
        }

        subjects = subjectRepository.saveAll(subjects);
        System.out.println("Created " + subjects.size() + " subjects.");

        // Generate marks and attendance records.
        List<AcademicRecord> records = new ArrayList<>();

        for (Student student : students) {

            for (Subject subject : subjects) {

                if (!subject.getBranch().equals(student.getBranch()) ||
                    !subject.getSemester().equals(student.getSemester())) {
                    continue;
                }

                AcademicRecord record = new AcademicRecord();
                record.setStudent(student);
                record.setSubject(subject);
                record.setAcademicYear(2026);

                record.setInternalMarks(random.nextInt(31));
                record.setExternalMarks(random.nextInt(71));

                int conducted = 30;
                int attended = 18 + random.nextInt(13);

                record.setClassesConducted(conducted);
                record.setClassesAttended(attended);

                records.add(record);
            }
        }

        recordRepository.saveAll(records);

        System.out.println("Created " + records.size() + " academic records.");
        System.out.println("Fictional university demo data generated successfully!");
    }
}
