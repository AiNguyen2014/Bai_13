package murach.admin;

import murach.business.User;
import murach.data.UserDB;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String url = "/index.jsp";

        // Chỉ xử lý action "update" trong doPost
        if (action != null && action.equals("update_user")) {
            // Lấy thông tin từ form
            String email = request.getParameter("email");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");

            // Lấy user cũ và cập nhật
            User user = UserDB.selectUser(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            UserDB.update(user);

            // Lấy danh sách mới và chuyển tiếp về trang admin
            List<User> users = UserDB.selectUsers();
            request.setAttribute("users", users);
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "display_users";  // Mặc định
        }

        String url = "/index.jsp"; // Mặc định là trang admin

        if (action.equals("display_users")) {
            // Lấy danh sách users
            List<User> users = UserDB.selectUsers();
            // Đặt danh sách làm request attribute
            request.setAttribute("users", users);
        }
        else if (action.equals("display_user")) {
            // Lấy email từ parameter
            String email = request.getParameter("email");
            // Lấy user theo email
            User user = UserDB.selectUser(email);
            // Đặt user làm request attribute
            request.setAttribute("user", user);
            // Chuyển đến trang user.jsp để sửa
            url = "/user.jsp";
        }
        else if (action.equals("delete_user")) {
            // Lấy email từ parameter
            String email = request.getParameter("email");
            // Lấy user theo email
            User user = UserDB.selectUser(email);
            // Xóa user
            UserDB.delete(user);
            // Lấy lại danh sách user mới nhất
            List<User> users = UserDB.selectUsers();
            // Đặt danh sách làm request attribute
            request.setAttribute("users", users);
        }

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}