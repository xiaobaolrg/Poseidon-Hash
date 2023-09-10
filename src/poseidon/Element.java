package poseidon;
import java.math.BigInteger;

public class Element {
    // Element represents a field element stored on 4 words (uint64)
    // Element are assumed to be in Montgomery form in all methods
    // field modulus q =
    //
    // 21888242871839275222246405745257275088548364400416034343698204186575808495617

    // Number of limbs (64-bit words) needed to represent Element
    private static final int Limbs = 4;

    // Number of bits needed to represent Element
    private static final int Bits = 254;

    // Number of bytes needed to represent Element
    private static final int Bytes = Limbs * 8;

    // Field modulus stored as BigInteger
//    private static final AtomicReference<BigInteger> modulus = new AtomicReference<>();

    private static BigInteger modulus;

    // 静态代码块，用于一次性的初始化操作
    static {
        modulus = new BigInteger("21888242871839275222246405745257275088548364400416034343698204186575808495617");
    }

    public static BigInteger modulus() {
        return new BigInteger(modulus.toByteArray());
    }

    // q (modulus)
    public static Element qElement = new Element(new BigInteger[]{
            new BigInteger("4891460686036598785"),
            new BigInteger("2896914383306846353"),
            new BigInteger("13281191951274694749"),
            new BigInteger("3486998266802970665")
    });


    // rSquare
    public static Element rSquare = new Element(new BigInteger[]{
            new BigInteger("1997599621687373223"),
            new BigInteger("6052339484930628067"),
            new BigInteger("10108755138030829701"),
            new BigInteger("150537098327114917")
    });



    public Element(BigInteger[] value) {
        this.value = value;
    }

    // BigInteger pool for reusing BigInteger instances
    private static final ThreadLocal<BigInteger> bigIntPool = ThreadLocal.withInitial(() -> BigInteger.ZERO);


    // NewElement returns a new Element
    public static Element newElement() {
        return new Element();
    }

    private BigInteger[] value = new BigInteger[Limbs];

    // Constructor to initialize an Element
    public Element() {
        value = new BigInteger[4];
        for (int i = 0; i < 4; i++) {
            value[i] = BigInteger.ZERO;
        }
    }
    // NewElementFromUint64 returns a new Element from a uint64 value
    public static Element newElementFromUint64(long v) {
        Element z = new Element();
        BigInteger bigIntValue = BigInteger.valueOf(v);
        z.setBigInteger(bigIntValue);
        z.Mul(z,rSquare);
        return z;
    }

    public Element setBigInteger(BigInteger v) {
        this.value = new BigInteger[]{v};
        this.Mul(this,rSquare); // 正确的参数个数是两个
        return this;
    }

    // Mul multiplies this Element with another Element mod q
    // External function that performs the actual multiplication
    public Element Mul(Element x, Element y) {
        mul(this, x, y);
        return this;
    }
    // Mul z = x * y mod q


    public static void mul(Element z, Element x, Element y) {
        _mulGeneric(z, x, y);
    }

    private static BigInteger multiplyWithOverflow(BigInteger a, BigInteger b, BigInteger[] overflow) {
        BigInteger[] result = a.multiply(b).divideAndRemainder(new BigInteger("2").pow(64));
        overflow[0] = result[0];
        return result[1];
    }

   



    public static void _mulGeneric(Element z, Element x, Element y) {
        BigInteger[] t = new BigInteger[4];
        BigInteger[] c = new BigInteger[3];

        // round 0
        BigInteger v = x.value[0];
        c[1] = c[0] = v.multiply(y.value[0]);
        BigInteger m = c[0].multiply(new BigInteger("14042775128853446655"));
        c[2] = Arith.madd0(m, new BigInteger("4891460686036598785"), c[0]);
        BigInteger[] result = Arith.madd1(v, y.value[1], c[1]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("2896914383306846353"), c[2], c[0]);
        c[2] = result[0];
        t[0] = result[1];
        result = Arith.madd1(v, y.value[2], c[1]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("13281191951274694749"), c[2], c[0]);
        c[2] = result[0];
        t[1] = result[1];
        result = Arith.madd1(v, y.value[3], c[1]);
        t[3] = result[0];
        t[2] = result[1];

        // round 1
        v = x.value[1];
        result = Arith.madd1(v, y.value[0], t[0]);
        c[1] = result[0];
        c[0] = result[1];
        m = c[0].multiply(new BigInteger("14042775128853446655"));
        c[2] = Arith.madd0(m, new BigInteger("4891460686036598785"), c[0]);
        result = Arith.madd2(v, y.value[1], c[1], t[1]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("2896914383306846353"), c[2], c[0]);
        c[2] = result[0];
        t[0] = result[1];
        result = Arith.madd2(v, y.value[2], c[1], t[2]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("13281191951274694749"), c[2], c[0]);
        c[2] = result[0];
        t[1] = result[1];
        result = Arith.madd2(v, y.value[3], c[1], t[3]);
        t[3] = result[0];
        t[2] = result[1];

        // round 2
        v = x.value[2];
        result = Arith.madd1(v, y.value[0], t[0]);
        c[1] = result[0];
        c[0] = result[1];
        m = c[0].multiply(new BigInteger("14042775128853446655"));
        c[2] = Arith.madd0(m, new BigInteger("4891460686036598785"), c[0]);
        result = Arith.madd2(v, y.value[1], c[1], t[1]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("2896914383306846353"), c[2], c[0]);
        c[2] = result[0];
        t[0] = result[1];
        result = Arith.madd2(v, y.value[2], c[1], t[2]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("13281191951274694749"), c[2], c[0]);
        c[2] = result[0];
        t[1] = result[1];
        result = Arith.madd2(v, y.value[3], c[1], t[3]);
        t[3] = result[0];
        t[2] = result[1];

        // round 3
        v = x.value[3];
        result = Arith.madd1(v, y.value[0], t[0]);
        c[1] = result[0];
        c[0] = result[1];
        m = c[0].multiply(new BigInteger("14042775128853446655"));
        c[2] = Arith.madd0(m, new BigInteger("4891460686036598785"), c[0]);
        result = Arith.madd2(v, y.value[1], c[1], t[1]);
        c[1] = result[0];
        c[0] = result[1];
        result = Arith.madd2(m, new BigInteger("2896914383306846353"), c[2], c[0]);
        z.value[0] = result[0];
        c[0] = result[1];
        result = Arith.madd2(v, y.value[2], c[1], t[2]);
        z.value[1] = result[0];
        c[1] = result[1];
        result = Arith.madd2(m, new BigInteger("13281191951274694749"), c[2], c[0]);
        z.value[2] = result[0];
        c[0] = result[1];
        result = Arith.madd2(v, y.value[3], c[1], t[3]);
        z.value[3] = result[0];
        z.value[2] = result[1];

        // if z > q --> z -= q
        // note: this is NOT constant time
        BigInteger q3 = new BigInteger("3486998266802970665");
        BigInteger q2 = new BigInteger("13281191951274694749");
        BigInteger q1 = new BigInteger("2896914383306846353");
        BigInteger q0 = new BigInteger("4891460686036598785");
        if (!(z.value[3].compareTo(q3) < 0 || (z.value[3].compareTo(q3) == 0 && (z.value[2].compareTo(q2) < 0 || (z.value[2].compareTo(q2) == 0 && (z.value[1].compareTo(q1) < 0 || (z.value[1].compareTo(q1) == 0 && z.value[0].compareTo(q0) < 0))))))) {
            BigInteger b = BigInteger.ZERO;
            z.value[0] = z.value[0].subtract(q0).subtract(b);
            b = z.value[0].compareTo(BigInteger.ZERO) < 0 ? BigInteger.ONE : BigInteger.ZERO;
            z.value[1] = z.value[1].subtract(q1).subtract(b);
            b = z.value[1].compareTo(BigInteger.ZERO) < 0 ? BigInteger.ONE : BigInteger.ZERO;
            z.value[2] = z.value[2].subtract(q2).subtract(b);
            b = z.value[2].compareTo(BigInteger.ZERO) < 0 ? BigInteger.ONE : BigInteger.ZERO;
            z.value[3] = z.value[3].subtract(q3).subtract(b);
        }
    }
}
