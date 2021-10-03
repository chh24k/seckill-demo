package com.example.seckill.utils;

import com.example.seckill.pojo.User;
import com.example.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Honghui
 */
public class CreatUserUtil {


    public static void createUser(int size) throws Exception {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            User user = new User();
            user.setId(18000000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c");
            user.setRegisterDate(new Date());
            user.setLoginCount(1);
            users.add(user);
            System.out.println("user = " + user);
        }
//        Connection conn = getConn();
//        System.out.println(conn);
//        String sql = "insert into t_user(id,nickname,password,salt,register_date,login_count) values(?,?,?,?,?,?)";
//        PreparedStatement prst = conn.prepareStatement(sql);
//        for (int i = 0; i < size; i++) {
//            User user = users.get(i);
//            prst.setLong(1, user.getId());
//            prst.setString(2, user.getNickname());
//            prst.setString(3, MD5util.inputPassToDBPass("123456", user.getSalt()));
//            prst.setString(4, user.getSalt());
//            prst.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
//            prst.setInt(6, user.getLoginCount());
//            prst.addBatch();
//        }
//        prst.executeBatch();
//        prst.clearParameters();
//        prst.close();
//        conn.close();
//        System.out.println("insert");

        String url = "http://localhost:8080/login/doLogin/";
        File file = new File("C:\\Users\\Honghui\\Desktop\\config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (int i = 0; i < size; i++) {
            User user = users.get(i);
            URL ur = new URL(url);
            HttpURLConnection co = (HttpURLConnection) ur.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            // 前端加密部分
            String params = "mobile=" + user.getId() + "&password=" + MD5util.inputPassToFromPass("123456");
            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) > 0) {
                bout.write(buff, 0, len);
            }
            in.close();
            bout.close();

            String s = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(s, RespBean.class);
            String userticket = (String) respBean.getObj();
            System.out.println("userticket" + userticket);
            String row = user.getId() + "," + userticket + "\r\n";
            raf.seek(raf.length());
            raf.write(row.getBytes());
            System.out.println("write to file" + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    public static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";
        String username = "chh";
        String password = "123";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
