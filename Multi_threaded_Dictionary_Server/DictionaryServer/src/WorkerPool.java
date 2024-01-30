/**
 * Author: Chenyang Dong
 * Student ID: 1074314
 */

import java.net.Socket;
import java.util.LinkedList;

/**
 * The type Worker pool.
 */
public class WorkerPool {
    private static WorkerPool instance = null;
    private LinkedList<WorkerThread> pool;

    private WorkerPool() {
        pool = new LinkedList<>();
    }

    /**
     * Initialize pool.
     *
     * @param poolSize the pool size
     * @param dict     the dict
     */
    public void initializePool(int poolSize, Dictionary dict) {
        for (int i = 0; i < poolSize; i++) {
            WorkerThread thread = new WorkerThread("Thread " + i, dict);
            thread.start();
            pool.add(thread);
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static WorkerPool getInstance() {
        if (instance == null) {
            instance = new WorkerPool();
        }
        return instance;
    }

    /**
     * Check if there is free worker.
     *
     * @return the boolean
     */
    public synchronized boolean hasFreeWorker() {
        return !pool.isEmpty();
    }

    /**
     * Assign work.
     *
     * @param clientSocket the client socket
     */
    public synchronized void assignWork(Socket clientSocket) {
        if (hasFreeWorker()) {
            WorkerThread worker = pool.pop();
            synchronized (worker) {
                worker.setConnection(clientSocket);
                worker.notify();
            }

            System.out.println("New connection assigned to worker: " + worker.getName());
        }
    }

    /**
     * Add worker to the pool.
     *
     * @param worker the worker
     */
    public synchronized void addWorker(WorkerThread worker) {
        pool.add(worker);
        System.out.println(worker.getName() + " returned to the pool.");
        System.out.println("*".repeat(50));
    }

}
