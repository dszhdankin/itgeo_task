package money;

import java.util.*;
import java.io.*;

public class Main {
	public static void main(String[] args) {
		try {
			
			int k = 0, n = 0;
			InputStreamReader source = null;
			if (args.length == 0) {
				source = new InputStreamReader(System.in);
			} else if (args[0].equals("console")) {
				source = new InputStreamReader(System.in);
			} else {
				source = new InputStreamReader(new FileInputStream(args[0]));
			}
			BufferedReader reader = new BufferedReader(source);
			String line = reader.readLine();
			List<String> nums = Arrays.asList(line.split("\\s*\\s"));
			if (nums.size() < 2) {
				System.out.println("Parsing error");
				return;
			}
			k = Integer.parseInt(nums.get(0));
			n = Integer.parseInt(nums.get(1));
			line = reader.readLine();
			nums = Arrays.asList(line.split("\\s*\\s"));
			int[] numsArr = new int[nums.size()];
			for (int i = 0; i < nums.size(); i++)
				numsArr[i] = Integer.parseInt(nums.get(i));
			
			int[] dp = new int[n + 1];
			Arrays.fill(dp, 0);
			dp[0] = 1;
			for (int monet : numsArr) {
				for (int i = 1; i <= n; i++) {
					if (i - monet >= 0)
						dp[i] += dp[i - monet];
				}
			}
			
			System.out.println(dp[n]);
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		
	}
}
