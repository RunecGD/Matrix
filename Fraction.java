package LenAl;

class Fraction {
    int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) throw new IllegalArgumentException("Denominator cannot be zero.");
        this.numerator = numerator;
        this.denominator = denominator;
        reduce();
    }

    private void reduce() {
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator /= gcd;
        denominator /= gcd;
    }

    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @Override
    public String toString() {
        if (denominator == 1) return Integer.toString(numerator);
        return numerator + "/" + denominator;
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    public Fraction add(Fraction other) {
        return new Fraction(this.numerator * other.denominator + other.numerator * this.denominator,
                this.denominator * other.denominator);
    }

    public Fraction subtract(Fraction other) {
        return new Fraction(this.numerator * other.denominator - other.numerator * this.denominator,
                this.denominator * other.denominator);
    }

    public Fraction divide(Fraction other) {
        return new Fraction(this.numerator * other.denominator, this.denominator * other.numerator);
    }
}