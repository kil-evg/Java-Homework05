package ait.bank.service;

import ait.bank.model.Account;

public class Transfer implements Runnable {
    private Account accFrom;
    private Account accTo;
    private int sum;

    public Transfer(Account accFrom, Account accTo, int sum) {
        this.accFrom = accFrom;
        this.accTo = accTo;
        this.sum = sum;
    }

    @Override
    public void run() {
        //both Threads have to establish common Resource locking protocol
        Account firstLock, secondLock;
        if (accFrom.hashCode() < accTo.hashCode()) {
            firstLock = accFrom;
            secondLock = accTo;
        } else {
            firstLock = accTo;
            secondLock = accFrom;
        }


        firstLock.lock();
        try {
            Thread.sleep(1000);
            secondLock.lock();
            try {
                if (accFrom.getBalance() >= sum) {
                    accFrom.debit(sum);
                    accTo.credit(sum);
                }
            } finally {
                secondLock.unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            firstLock.unlock();
        }
    }
}
