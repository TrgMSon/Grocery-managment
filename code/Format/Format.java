package Format;

public class Format {
    public static String normalizeNumber(String number) {
        String ans = "";
        int cnt = 1;
        for (int i=number.length() - 1; i>=0; i--) {
            ans = number.charAt(i) + ans;
            if (cnt % 3 == 0 && i != 0) ans =  ',' + ans; 
            cnt += 1;
        }
        return ans;
    }

    public static String originalForm(String number) {
        String ans = "";
        for (int i=0; i<number.length(); i++) {
            if (number.charAt(i) != ',') ans += number.charAt(i);
        }
        return ans;
    }
}
