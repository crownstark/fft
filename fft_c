// Christopher Russo
// SoC Design
// FFT in C

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <malloc.h>
#include <time.h>

typedef struct{
    double re;
    double im;
} complex;

#define N 256
#define numsamp 10000

#ifndef PI
# define PI	3.14159265358979323846264338327950288
#endif

// checks if even
// was implemented before when trying to use stages instead of recursion
// note that num_stages = log_2(N)
// i.e. if N = 8, num_stages of FFT is 3


int iseven(int n){
    if(n%2==0){
        return 1;
    }
    else{
        return 0;
    }
}


// checks to see if N is a power of 2
// for error checking
int ispow_two(int n){

    if(n==0){
        return 0;
    }
    while (n!=1){
        n = n/2;
        if((n%2 != 0) && (n != 1)){
            return 0;
        }
    }
    return 1;
}

// prints a complex struct
void print_complex(complex *z, int n){
    
    for (int i = 0; i < n; ++i){
        printf("vec[%d] = %0.7f + j%0.7f\n", i, z[i].re, z[i].im);
    }
    
}

// twiddle """ROM""" generator
complex* twiddle_gen(int n){
    int K = n/2;
    complex *w = malloc(sizeof(complex)*K);
    for (unsigned int i = 0; i < K; i++){
        w[i].re = cos(2*i*PI/n);
        w[i].im = sin(2*i*PI/n);
    }
    
    return w;
}

// complex struct subtraction
complex complex_sub(complex n1, complex n2){
    complex res;
    res.re = n1.re - n2.re;
    res.im = n1.im - n2.im;
    
    return res;
}

// complex struct multiplication
complex complex_mult(complex n1, complex n2){

    complex res;
    res.re = (n1.re*n2.re) - (n1.im*n2.im);
    res.im = (n1.re*n2.im) + (n1.im*n2.re);
    
    return res;
}

// complex struct addition
complex complex_add(complex n1, complex n2){
    complex res;
    
    res.re = n1.re+n2.re;
    res.im = n1.im+n2.im;
    
    return res;
}

/********************************************************************/
// butterfly block implementation -- not needed? kind of janky anyway
/*complex* butterfly(complex n1, complex n2, complex w){
    complex *res = malloc(sizeof(complex)*2);
    
    res[0] = complex_add(n1, complex_mult(w, n2));
    res[1] = complex_sub(n1, complex_mult(w, n2));
    //print_complex(res, 2);
    
    return res;
}
/********************************************************************/


// generates some arbitrary cos signal of some sampling frequency fs
// and at some linear frequency float
// number of samples is globally defined
complex* signal_gen(float fs, float f){
    float ts = 1/fs;
    static complex s[numsamp];
    
    int cnt = 0;
    // why is this not a for loop? no idea, tbf
    while(cnt != numsamp){
        s[cnt].re = cos(cnt*ts*2*PI*f);
        s[cnt].im = 0;
        cnt++;
    }
    return s;
}

// magnitude of complex struct
double* mag(complex *n){
    double *res = malloc(sizeof(double)*N);
    for (unsigned int i = 0; i < N; i++){
        res[i] = sqrt(n[i].re*n[i].re + n[i].im*n[i].im);
    }
    return res;
}

// radix-2 dit fft
complex* ditfft2(complex *s, int n){

    complex* w; // twiddle factor
    w = twiddle_gen(n);
    
    // FFT output vector
    complex *X = malloc(sizeof(complex)*N);
    
    //split into odd/even
    //complex s_even[n/2];
    //complex s_odd[n/2];
    complex *s_even = malloc(sizeof(complex)*n/2);
    complex *s_odd = malloc(sizeof(complex)*n/2);
    
    for (unsigned int i = 0; i < n/2; i++){
        s_even[i].re = s[i*2].re;
        s_even[i].im = s[i*2].im;
        s_odd[i].re = s[i*2+1].re;
        s_odd[i].im = s[i*2+1].im;
    }
    
    // base case
    if(n==1){
        return s;
    }
    
    // recursion yuck
    s_even = ditfft2(s_even, n/2);
    s_odd = ditfft2(s_odd, n/2);
    
    // butterfly ops -- had a function for it, but was jank
    for (int k = 0; k < n/2; k++){
        X[k] = complex_add(s_even[k], complex_mult(w[k], s_odd[k]));
        X[k+n/2] = complex_sub(s_even[k], complex_mult(w[k], s_odd[k]));
    }
    return X;
}


/***********************************************************************/
/***********************************************************************/
/*
This "bit-reversal" will be stupid important for chisel3 implementation
*/
/***********************************************************************/
/***********************************************************************/

    // bit reversal -- shift right by 1
    // s[i]
    // s_even[i >> 1]/s_odd[i >> 1]
    //let i = 1
    // s[1] --> s_odd[0]
    //let i = 2
    // s[2] --> s_even[1]
    //let i = 6;
    // s[6] -- > s_even[0011];
    // s[7] -- > s_odd[0011];
    
/***********************************************************************/
void generate_sig_lookup(complex *s){
    FILE *arb_cos = fopen("cosine_values.txt", "w+");
    for (unsigned int i = 0; i < numsamp; i++){
        if (iseven(i)){
            fprintf(arb_cos, "%lf\n", s[i].re);
        }
        else{
            if(i==numsamp-1){
                fprintf(arb_cos, "%lf", s[i].im);
            }
            else{
                fprintf(arb_cos, "%lf\n", s[i].im);
            }
            
        }
    }
}

int main(){
    
    if (!ispow_two(N)){
        printf("N must be a power of 2. Try again.\n");
        return 0;
    }
    
    
    if (numsamp < N){
        printf("Needs more samples, N can't be greater than number of samples.\n");
        return 0;
    }
    
    /*char* char1 = "fft_outp";
    char N_str = (char)N;*/
    
    FILE *out = fopen("fft_outp.txt", "w");
    
    complex *result = malloc(sizeof(complex)*N);
    double *magnitude = malloc(sizeof(double)*N);
    
    //generate signal
    complex *s;
    s = signal_gen(10000.0, 400.0);
    
    // execution time
    clock_t t;
    t = clock();
    
    result = ditfft2(s, N);
    
    t = clock() - t;
    double ex_time = ((double)t)/CLOCKS_PER_SEC;
    ex_time = ex_time*1000000.0; // microseconds
    
    
    printf("Complex result:\n");
    print_complex(result, N);
    magnitude = mag(result);
    printf("Magnitude:\n");
    for (unsigned int i = 0; i < N; i++){
        printf("vec[%d]: %f\n", i, magnitude[i]);
        fprintf(out, "%f\n", magnitude[i]);
    }
    printf("Time to execute for N = %d:\n %0.8f us\n %0.8f ms", N, ex_time, ex_time/1000.0);
    
    generate_sig_lookup(s);
    
    return 0;
}
