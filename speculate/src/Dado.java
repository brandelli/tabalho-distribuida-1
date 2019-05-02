import java.util.Random;
public class Dado {
	private static Random random = new Random();
	public static int jogaDado() {
		return random.nextInt(5) + 1;
		//return 6;
	}
}
