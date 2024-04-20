package test;

import java.util.BitSet;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BloomFilter {
    private final int bitSetLength;
    private final BitSet bitSet;
    private final MessageDigest[] hashFunc;

    public BloomFilter(int length, String...algorithms){
        this.bitSetLength=length;
        this.bitSet=new BitSet(bitSetLength);
        this.hashFunc=new MessageDigest[algorithms.length];
        for(int i=0; i<algorithms.length; i++){
            try {
                hashFunc[i] = MessageDigest.getInstance(algorithms[i]);
            }catch (NoSuchAlgorithmException e){
                System.err.println(e.getMessage());
            }
        }
    }

    public void add(String str){
        for(MessageDigest md: hashFunc){
            byte[] hash=md.digest(str.getBytes());
            int index= Math.abs(new BigInteger(hash).intValue())%bitSetLength;
            this.bitSet.set(index,true);
        }
    }

    public boolean contains(String str){
        for(MessageDigest md: hashFunc){
            byte[] hash=md.digest(str.getBytes());
            int index= Math.abs(new BigInteger(hash).intValue())% bitSetLength;
            if(!this.bitSet.get(index)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        for( int i=0; i<this.bitSet.length(); i++){
            sb.append(bitSet.get(i) ? "1" : "0");
        }
        return sb.toString();
    }

}
