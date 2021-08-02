package littlegame.main;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class gameStart {

	public static void main(String[] args) {

		// TODO 加载历史记录 没有则创建

		// 最初先获取一个四位不同的数据
		ArrayList<Integer> target = getRandomNumber();
		System.out.println(target.toString());

		// 取得当前时间
		LocalDateTime before = LocalDateTime.now();

		// 从控制台开始读入数据
		for (int i = 1; i < 11; i++) {
			String read;
			boolean isOK;
			do {
				System.out.println(String.format("第%d次输入：", i));
				Scanner scan = new Scanner(System.in);
				read = scan.nextLine();
				System.out.println("输入数据：" + read);

				isOK = judge(stringToArray(read));

				if (!isOK) {
					System.out.println("输入的数据不是4位不同的数字,请重新输入！ ");
				}
			} while (!isOK);

			String resault = match(target, stringToArray(read));
			System.out.println(resault);

			if (resault.equals("4A0B")) {
				LocalDateTime after = LocalDateTime.now();
				Duration duration = Duration.between(before, after);
				System.out.println(String.format("输入的结果正确，共输入%d次，得分%d，游戏结束", i, duration.toMillis() / 1000));

				// TODO 将成功的数据写入文件
				return;
			} else {
				// TODO 将失败的数据写入文件
			}
		}
		LocalDateTime end = LocalDateTime.now();
		Duration durationend = Duration.between(before, end);
		System.out.println(String.format("游戏失败，用时%d秒", durationend.toMillis()));
	}

	public static ArrayList<Integer> getRandomNumber() {

		ArrayList<Integer> list = new ArrayList<Integer>();
		Random random = new Random();

		for (int i = 0; i < 4; i++) {
			int randomNumber;
			do {
				randomNumber = Math.abs(random.nextInt() % 10);
				System.out.println(randomNumber);
			} while (list.contains(randomNumber));
			list.add(randomNumber);
		}
		return list;
	}

	public static String match(ArrayList<Integer> target, ArrayList<Object> variable) {

		int numberA = 0;
		int numberB = 0;

		for (int i = 0; i < 4; i++) {
			System.out.println("target：" + target.get(i) + "   " + "variable：" + variable.get(i));

			if (target.get(i).toString().equals(variable.get(i) + "")) {
				numberA++;
			}

			if (target.contains(Integer.valueOf((char) variable.get(i) - '0'))) {
				numberB++;
			}
		}

		return numberA + "A" + (numberB - numberA) + "B";
	}

	/**
	 * 判断是否为四位不同的数据
	 * 
	 * @param variable
	 * @return 判断结果
	 */
	public static boolean judge(ArrayList<Object> variable) {

		if (variable.size() != 4) {
			return false;
		}
		for (int i = 0; i < 4; i++) {

			Object object = variable.get(i);
			if ((char) object < '0' || '9' < (char) object) {
				return false;
			}

			ArrayList<Integer> variableTemp = (ArrayList<Integer>) variable.clone();
			variableTemp.remove(i);

			if (variableTemp.contains(object)) {
				return false;
			}
		}
		return true;
	}

	public static ArrayList<Object> stringToArray(String string) {
		ArrayList<Object> list = new ArrayList<Object>();
		for (int i = 0; i < string.length(); i++) {
			list.add(string.charAt(i));
		}
		return list;
	}
}
