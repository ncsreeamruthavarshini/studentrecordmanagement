package com.studentmanagement;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while (true) {
            System.out.println("\n--- Student Record Management ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter student ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Enter student name: ");
                String name = sc.nextLine();

                System.out.print("Enter course: ");
                String course = sc.nextLine();

                students.add(new Student(id, name, course));
                System.out.println("Student added successfully!");

            } else if (choice == 2) {
                if (students.isEmpty()) {
                    System.out.println("No student records found.");
                } else {
                    for (Student s : students) {
                        System.out.println(
                            "ID: " + s.getId()
                            + ", Name: " + s.getName()
                            + ", Course: " + s.getCourse()
                        );
                    }
                }

            } else if (choice == 3) {
                System.out.print("Enter student ID to search: ");
                int searchId = sc.nextInt();

                boolean found = false;

                for (Student s : students) {
                    if (s.getId() == searchId) {
                        System.out.println("Student Found!");
                        System.out.println("ID: " + s.getId());
                        System.out.println("Name: " + s.getName());
                        System.out.println("Course: " + s.getCourse());

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Student not found.");
                }

            } else if (choice == 4) {
                System.out.print("Enter student ID to update: ");
                int updateId = sc.nextInt();
                sc.nextLine();

                boolean found = false;

                for (Student s : students) {
                    if (s.getId() == updateId) {
                        System.out.print("Enter new student name: ");
                        String newName = sc.nextLine();

                        System.out.print("Enter new course: ");
                        String newCourse = sc.nextLine();

                        s.setName(newName);
                        s.setCourse(newCourse);

                        System.out.println("Student details updated successfully!");

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    System.out.println("Student not found.");
                }

            } else if (choice == 5) {
                System.out.println("Program closed.");
                break;

            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}