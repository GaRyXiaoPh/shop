package cn.kt.mall.common.util;

import java.util.Arrays;
import java.util.Random;

public class RedUtil {

	public static int[] generate(int total_money_cent, int total_people) {
		int array[] = new int[total_money_cent];
		if(total_money_cent == total_people ) {
			for (int i = 0; i < array.length; i++) {
				array[i] = 1;
			}
			return array;
		}
		for (int i = 0; i < total_money_cent; i++) {
			array[i] = i;
		}
		Random random = new Random();
		for (int i = 1; i < total_people; i++) {
			int index = random.nextInt(total_money_cent - i) + i;
			int temp = array[i];
			array[i] = array[index];
			array[index] = temp;
		}
		array[total_people] = total_money_cent;
		Arrays.sort(array, 0, total_people + 1);

		int[] result = new int[total_people];
		for (int i = 1; i <= total_people; i++) {
			result[i - 1] = array[i] - array[i - 1];
		}
		return result;
	}

	public static void main(String[] args) {
		int[] result = generate(10,10);
		for (int i : result) {
			System.out.println(i);
		}
	}
}
