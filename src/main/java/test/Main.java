package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main
{
    public static final List<String>  message = new ArrayList<>();

    public static void main (String[] args)
    {

        message.add("01234");
        message.add("5678");

        List<String> display = new ArrayList<>();
        message.forEach((oo) -> {
            display.add("");
        });
        for (int i = 0; i < message.size(); i++)
        {
            char[] words = message.get(i).toCharArray();
            int j = 0 ;

            while (j < words.length) {

                if (j == 0)
                    display.set(i, words[j] + "_");
                else {
                    display.set(i, display.get(i).replace("_", words[j] + "_"));
                }
                j++;
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(display.get(i));

            }

        }

    }

}
