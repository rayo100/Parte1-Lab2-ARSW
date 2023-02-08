/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;

    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }

        try {
            while(true) {
                this.sleep(TMILISECONDS);
                
                PrimeFinderThread.setMustWait(true);

                for (PrimeFinderThread primeFinderThread : pft)

                    if(!primeFinderThread.isInterrupted())
                        this.sleep(20);

                System.out.println("Amount of primes found is: " + PrimeFinderThread.getCounter());
                new Scanner(System.in).nextLine();
            
                PrimeFinderThread.setMustWait(false);
                synchronized(this) {
                    this.notifyAll();
                }
                                
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
}
