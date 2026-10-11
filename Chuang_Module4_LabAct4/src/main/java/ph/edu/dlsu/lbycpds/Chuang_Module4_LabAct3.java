package ph.edu.dlsu.lbycpds;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Task {
    String id;
    int remainingTime;
    int completionTime;

    public Task(String id, int burstTime) {
        this.id = id;
        this.remainingTime = burstTime;
        this.completionTime = 0;
    }
}

public class Chuang_Module4_LabAct3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=========================================================");
        System.out.println("ROUND ROBIN SCHEDULING SYSTEM");
        System.out.println("=========================================================");

        System.out.print("Enter Time Slice (seconds): ");
        int timeSlice = sc.nextInt();
        sc.nextLine();

        List inputTasks = new ArrayList();

        System.out.println("\n========= Task Details Input (Enter 0 to stop) =========");
        int taskCount = 1;

        while (true) {
            System.out.print("Enter Task Name/Number " + taskCount + ": ");
            String id = sc.nextLine().trim();
            if (id.isEmpty()) {
                id = "T" + taskCount;
            }

            System.out.print("Enter Task Time for " + id + ": ");
            int burstTime = sc.nextInt();
            sc.nextLine();

            if (burstTime <= 0) {
                System.out.println("Finished entering tasks.");
                break;
            }

            inputTasks.add(new Task(id, burstTime));
            taskCount++;
        }

        if (inputTasks.isEmpty()) {
            System.out.println("No tasks entered. Program exiting.");
            sc.close();
            return;
        }

        MyQueue queue = new MyQueue(inputTasks.size());
        for (int i = 0; i < inputTasks.size(); i++) {
            queue.offer(inputTasks.get(i));
        }

        List executionSequence = new ArrayList();
        List finishedTasks = new ArrayList();
        int currentTime = 0;

        while (!queue.isEmpty()) {
            Task currentTask = (Task) queue.poll();
            executionSequence.add(currentTask.id);

            int timeToRun = Math.min(currentTask.remainingTime, timeSlice);
            currentTask.remainingTime -= timeToRun;
            currentTime += timeToRun;

            if (currentTask.remainingTime > 0) {
                queue.offer(currentTask);
            } else {
                currentTask.completionTime = currentTime;
                finishedTasks.add(currentTask);
            }
        }

        System.out.println("\n=========================================================");
        System.out.println("ROUND ROBIN SCHEDULING SYSTEM DATA SUMMARY");
        System.out.println("=========================================================");

        System.out.print("Execution Order: ");
        for (int i = 0; i < executionSequence.size(); i++) {
            System.out.print(executionSequence.get(i));
            if (i < executionSequence.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("\n--------------------------------------------------------");
        System.out.println("Task Completion Time:");
        for (int i = 0; i < finishedTasks.size(); i++) {
            Task t = (Task) finishedTasks.get(i);
            System.out.println("   " + t.id + " finished after " + t.completionTime + " seconds");
        }

        System.out.println("=========================================================");
        System.out.println("All tasks finished in " + currentTime + " seconds.");

        sc.close();
    }
}