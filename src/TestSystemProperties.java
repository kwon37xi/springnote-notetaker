public class TestSystemProperties {
	
	public static void main(String [] args) {
		System.getProperties().list(System.out);
		
		char[] chars = {'h', 'e', 'l'};
		
		System.out.println(String.valueOf(chars));
	}

}
