package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受数据
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter(" pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //2.处理数据
        int cid = 0;
        if (cidStr != null && cidStr.length()>0 && !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 0;//当前页码，如果不传递，则默认为第一页
        if ( currentPageStr!= null && currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else {
            currentPage = 1;
        }

        int pageSize = 0;//每页显示条数，如果不传递，默认每页显示5条记录
        if ( pageSizeStr!= null && pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else {
            pageSize = 5;
        }

        //3.调用service查询PageBean对象
        PageBean<Route> pb = service.pageQuery(cid, currentPage, pageSize,rname);
        //4.将PageBean序列化json
        writeValue(pb,response);

    }

    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        Route route = service.findOne(rid);
        //
        //3.转为json数据写回客户端
        writeValue(route,response);


    }
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获得线路id
        String rid = request.getParameter("rid");
        //2.获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null){
            uid = 0;
        }else {
            uid = user.getUid();
        }
        //3.调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4.写回客户端
        writeValue(flag,response);
    }
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取rid
        String rid = request.getParameter("rid");
        //2.获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null){
            return;
        }else {
            uid = user.getUid();
        }
        //3.调用service完成添加
        favoriteService.addFavorite(rid,uid);
    }
}
