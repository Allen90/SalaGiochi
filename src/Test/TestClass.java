package Test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import server.exception.UserException;
import server.model.User;
import server.utils.PropertiesService;

public class TestClass {

	@Test
	public void testUser() throws UserException {
		
		// First Constructor
		User a = new User("username", "password", 800);
		
		assertTrue(a.getCredits() == 800);
		assertTrue(a.getPassword() == "password");
		assertTrue(a.getUsername() == "username");
		
		System.out.println(a);
		
		// Second Constructor
		User b = new User("username", "password");
		
		assertTrue(b.getCredits() == 80);
		assertTrue(b.getPassword() == "password");
		assertTrue(b.getUsername() == "username");
		
		System.out.println(b);
		
		// Third Constructor
		User c = new User("username");
		
		assertTrue(c.getCredits() == 80);
		assertTrue(c.getPassword() == "");
		assertTrue(c.getUsername() == "username");
		
		System.out.println(c);
		
		// Test add credits
		assertTrue(a.addCredits(100) == 900);
		System.out.println(a);
		
		assertTrue(a.addCredits(-100) == 900);
		System.out.println(a);
	}
	
	@Test
	public void testPropertiesService () {
		System.out.println(PropertiesService.instance().getProperties("a"));
		System.out.println(PropertiesService.instance().getProperties("b", "banane"));
	}
}