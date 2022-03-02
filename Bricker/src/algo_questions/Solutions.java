package algo_questions;

import java.util.Arrays;

public class Solutions {

    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        Arrays.sort(tasks);
        Arrays.sort(timeSlots);
        int timeIndex = timeSlots.length - 1;
        int sum = 0;
        for (int i = tasks.length - 1; i >= 0; i--) {
            if (timeIndex >= 0) {
                if (tasks[i] <= timeSlots[timeIndex]) {
                    sum++;
                    timeIndex--;
                }
            }
        }
        return sum;
    }

    public static int minLeap(int[] leapNum) {
        int[] solutions = new int[leapNum.length];
        if (leapNum.length == 0 || leapNum[0] == 0)
            return 0;
        solutions[0] = 0;
        for (int i = 1; i < leapNum.length; i++) {
            solutions[i] = Integer.MAX_VALUE;
            for (int j = 0; j < i; j++) {
                if (i <= j + leapNum[j] && solutions[j] != Integer.MAX_VALUE) {
                    solutions[i] = Math.min(solutions[i], solutions[j] + 1);
                    break;
                }
            }
        }
        return solutions[leapNum.length - 1];
    }

    public static int bucketWalk(int n) {
        int[] solutions = new int[n+2];
        solutions[0] = 0;
        solutions[1] = 1;
        for (int i = 2; i <= n + 1; i++)
        {
            solutions[i] = solutions[i-1] + solutions[i-2];
        }
        return solutions[n + 1];
    }

    public static int numTrees(int n){
        int[] solutions = new int[n + 1];
        Arrays.fill(solutions, 0);
        solutions[0] = 1;
        solutions[1] = 1;
        for (int i = 2; i <= n; i++)
        {
            for (int j = 1; j <= i; j++)
            {
                solutions[i] = solutions[i] + (solutions[i - j] *
                        solutions[j - 1]);
            }
        }
        return solutions[n];
    }


    public static void main(String[] args) {
        System.out.println(bucketWalk(6));
    }
}