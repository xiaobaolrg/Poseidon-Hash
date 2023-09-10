package poseidon;

import java.math.BigInteger;

public class Arith {
    // madd0 - 返回高32位
    public static BigInteger madd0(BigInteger a, BigInteger b, BigInteger c) {
        BigInteger hi = a.multiply(b);
        BigInteger lo = c;
        return hi.add(lo.shiftRight(32));
    }

    // madd1 - 返回高低32位
    public static BigInteger[] madd1(BigInteger a, BigInteger b, BigInteger c) {
        BigInteger hi = a.multiply(b);
        BigInteger lo = c;
        BigInteger carry = lo.shiftRight(32);
        hi = hi.add(carry);
        lo = lo.and(new BigInteger("FFFFFFFF", 16));
        return new BigInteger[]{hi, lo};
    }

    // madd2 - 返回高低32位
    public static BigInteger[] madd2(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
        BigInteger hi = a.multiply(b);
        BigInteger lo = c.add(d);
        BigInteger carry = lo.shiftRight(32);
        hi = hi.add(carry);
        lo = lo.and(new BigInteger("FFFFFFFF", 16));
        return new BigInteger[]{hi, lo};
    }

    // madd3 - 返回高低32位
    public static BigInteger[] madd3(BigInteger a, BigInteger b, BigInteger c, BigInteger d, BigInteger e) {
        BigInteger hi = a.multiply(b);
        BigInteger lo = c.add(d);
        BigInteger carry = lo.shiftRight(32);
        hi = hi.add(carry);
        lo = lo.and(new BigInteger("FFFFFFFF", 16));
        hi = hi.add(e);
        return new BigInteger[]{hi, lo};
    }
}
