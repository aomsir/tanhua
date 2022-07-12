package com.aomsir.test;

/**
 * @Author: Aomsir
 * @Date: 2022/7/12
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */
public class StringTest {
    public static void main(String[] args) {
        String token1 = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTc2MDkyNjksIm1vYmlsZSI6IjEzMTk3MzA1Mzk4IiwiaWQiOjExOH0.Xwq65jz47LvanG1crs3QR2bAnpUE-gD9NC2U1zd5TdS9Xy4fRzJ-0brZ0-egGpi776fsurMLWRj5gcqtDhiwhQ";
        String token2 = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTc2MDkyNjksIm1vYmlsZSI6IjEzMTk3MzA1Mzk4IiwiaWQiOjExOH0.Xwq65jz47LvanG1crs3QR2bAnpUE-gD9NC2U1zd5TdS9Xy4fRzJ-0brZ0-egGpi776fsurMLWRj5gcqtDhiwhQ";

        System.out.println(token1.equals(token2));
    }
}
