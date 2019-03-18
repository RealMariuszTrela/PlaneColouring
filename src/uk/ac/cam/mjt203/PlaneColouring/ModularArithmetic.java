package uk.ac.cam.mjt203.PlaneColouring;

public class ModularArithmetic {
    public static int power(int a, int n, int p) {
        if(n==0) return 1;
        if(n%2==0) return power(a*a%p, n/2, p);
        return a*power(a*a%p, n/2, p)%p;
    }
    public static int inverse(int a, int p) {
        return power(a, p-2, p);
    }
}
