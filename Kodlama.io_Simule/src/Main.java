import java.awt.color.ICC_ColorSpace;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javax.xml.crypto.Data;

import business.abstracts.CategoryService;
import business.abstracts.CourseService;
import business.abstracts.InstructorService;
import business.concretes.CategoryManager;
import business.concretes.CourseManager;
import business.concretes.InstructorManager;
import core.Logger.abstracts.Logger;
import core.Logger.concretes.DatabaseLogger;
import core.Logger.concretes.FileLogger;
import core.Logger.concretes.MailLogger;
import core.exceptions.ValidatorException;
import core.rules.abstracts.CategoryRules;
import core.rules.abstracts.CourseRules;
import core.rules.abstracts.InstructorRules;
import core.rules.concretes.CategoryRuleBusiness;
import core.rules.concretes.CourseRuleBusiness;
import core.rules.concretes.InstructorRuleBusiness;
import core.validator.abstracts.CategoryValid;
import core.validator.abstracts.CourseValid;
import core.validator.abstracts.InstructorValid;
import core.validator.concretes.CategoryValidator;
import core.validator.concretes.CourseValidator;
import core.validator.concretes.InstructorValidator;
import dataAccess.abstracts.CategoryDao;
import dataAccess.abstracts.CourseDao;
import dataAccess.abstracts.InstructorDao;
import dataAccess.concretes.hibernateDatabase.HibernateCategoryDao;
import dataAccess.concretes.jdbcDatabase.JdbcCourseDao;
import dataAccess.concretes.jdbcDatabase.JdbcInstructorDao;
import entities.Category;
import entities.Course;
import entities.Instructor;

public class Main {

	public static void main(String[] args) {
		course();
		System.out.println("********************************************");
		category();
		System.out.println("********************************************");
		instructor();
		

	}

	public static void course() {
		Course course1 = new Course();
		course1.setId(1);
		course1.setPrice(100);
		course1.setName("Java");
		course1.setInstructorId(1);
		course1.setCategoryId(1);
		
		Course course2 = new Course();
		course2.setId(2);
		course2.setPrice(0);
		course2.setName("Python");
		course2.setInstructorId(1);
		course2.setCategoryId(2);
		
		Course course3 = new Course();
		course3.setId(3);
		course3.setPrice(100);
		course3.setName("PHP");
		course3.setInstructorId(1);
		course3.setCategoryId(3);
		
		Course course4 = new Course();
		course4.setId(4);
		course4.setPrice(400);
		course4.setName("AAAA");
		course4.setInstructorId(1);
		course4.setCategoryId(4);
		
		Course course5 = new Course(5,"GO",500,1,5);
		Course course6 = new Course(6,"JavaScript",600,1,6);
		Course course7 = new Course(7,"C++",700,1,7);
		
		CourseDao jdbcCourseDao = new JdbcCourseDao();
		CourseRules courseRules = new CourseRuleBusiness(jdbcCourseDao);
		CourseValid courseValid = new CourseValidator();
		Logger[] loggers = new Logger[] {new DatabaseLogger(), new FileLogger()};
		
		CourseService courseService = new CourseManager(jdbcCourseDao, courseRules, courseValid, loggers);
		System.out.println("----------------------------------------");
		
		
		
		try {
			courseService.add(course1);  // Burada veri tabanına ekleme yaptık.
			courseService.add(course2);  // Kurs fiyatı 0'dan büyük olmalı. Bu yüzden hata verir.
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("----------------------------------------");
		
		
		try {
			System.out.println(courseService.getById(1).getName()); // id ye göre veritabanından getirir. Java
			System.out.println(courseService.getById(10).getName());// id ye göre veritabanından getirir falat hata verir. Kurs bulunamadı.

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("----------------------------------------");		
		try {
			courseService.add(course6); 	//JavaScript'i ekler
			courseService.delete(course6);  ////JavaScript'i siler
			courseService.add(course3);
			courseService.add(course4);
			courseService.add(course5);
			courseService.add(course6);
			courseService.add(course7);
			System.out.println("----------------------------------------");			
			
			Course course = courseService.getAllByName(course1.getName()); //ismi java olanı getirir.
			System.out.println(course1.getName());
			
			List<Course> courses = courseService.getAll();
			for (Course cours : courses) {
				System.out.println(cours.getName());
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println("----------------------------------------");
	}
	
	
	
	public static void category(){
		Category category1 = new Category();
		category1.setId(1);
		category1.setName("Programming");
		
		Category category2 = new Category();
		category2.setId(2);
		category2.setName("Yazılım");
		
		Category category3 = new Category(3,"Programming");
		
		
		CategoryDao categoryDao = new HibernateCategoryDao();
		CategoryRules categoryRules = new CategoryRuleBusiness(categoryDao);
		CategoryValid categoryValid = new CategoryValidator();
		Logger[] loggers = new Logger[] {new DatabaseLogger(), new FileLogger()};
		
		CategoryService categoryService = new CategoryManager(categoryDao, categoryRules, categoryValid, loggers);
		
		
		try {
			categoryService.add(category1); //Veritabanına ekler
			categoryService.add(category2); //Veritabanına ekler
			categoryService.add(category3); // Hata verir. Kategori zaten mevcut.
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		try {
			categoryService.update(category2); // Güncelleme yapmaz zaten Katogori kayıtlı
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		List<Category> categories = categoryService.getAll();
		
		for (Category cate : categories) {
			System.out.println(cate.getName());
		}
	}
	
	
	
	public static void instructor() {
		Instructor instructor1 = new Instructor();
		instructor1.setId(1);
		instructor1.setFirstName("Mehmet");
		instructor1.setLastName("Çopur");
		instructor1.setMail("mmmmm.@gmail.com");
		
		Instructor instructor2 = new Instructor(2,"Kenan", "Kaya", "instructor2.@gmail.com");
		Instructor instructor3 = new Instructor(3,"Hamza", "Koç", "instructor2.@gmail.com");
		Instructor instructor4 = new Instructor(4,"Yusuf", "Özcan", "instructor4.@gmail.com");
		
		
		InstructorDao instructorDao = new JdbcInstructorDao();
		InstructorRules instructorRules = new InstructorRuleBusiness(instructorDao);
		InstructorValid instructorValid = new InstructorValidator();
		Logger[] loggers = new Logger[] {new DatabaseLogger(), new FileLogger()};
		InstructorService instructorService = new InstructorManager(instructorDao, instructorRules, instructorValid, loggers);
		
		
		try {
			instructorService.add(instructor1); //Mehmet'i ekler
			instructorService.add(instructor2); // Kenan'ı ekler
			instructorService.add(instructor3); // Hata verir mail zaten kayıtlı
			instructorService.add(instructor4);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		List<Instructor> instructors = instructorService.getAll();
		for (Instructor instructorr : instructors) {
			System.out.println(instructorr.getFirstName());
		}
		
	}

	
}
