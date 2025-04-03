package softdreams.website.project_softdreams_restful_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import softdreams.website.project_softdreams_restful_api.domain.User;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqCreate;
import softdreams.website.project_softdreams_restful_api.dto.request.UserReqUpdate;
import softdreams.website.project_softdreams_restful_api.dto.response.ResGlobal;
import softdreams.website.project_softdreams_restful_api.dto.response.UserRes;
import softdreams.website.project_softdreams_restful_api.dto.response.UserSelectForAdmin;
import softdreams.website.project_softdreams_restful_api.exception.CustomException;
import softdreams.website.project_softdreams_restful_api.service.UserService;
import softdreams.website.project_softdreams_restful_api.util.ApiMessage;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/admin/user")
    @ApiMessage(message = "Thêm mới user thành công!")
    public ResponseEntity<UserRes> postMethodName(@Valid @RequestBody UserReqCreate userReq) throws CustomException {
        User newUser = this.userService.createUser(userReq);
        UserRes userRes = this.userService.ResUserCreate(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userRes);
    }
    
    // @GetMapping("/admin/user")
    // @ApiMessage(message = "Tìm kiếm user thành công!")
    // public ResponseEntity<List<UserRes>> fetchAllUser() {
    //     List<UserRes> userRes = this.userService.fetchAllUserRes();
    //     return ResponseEntity.status(HttpStatus.OK).body(userRes);
    // }

    @GetMapping("/admin/user")
    @ApiMessage(message = "Tìm kiếm user thành công!")
    public ResponseEntity<List<UserSelectForAdmin>> fetchAllUserForAdmin() {
        List<UserSelectForAdmin> userRes = this.userService.getAllUserSelectForAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(userRes);
    }

    @GetMapping({"/user/{id}", "/admin/user/{id}"})
    @ApiMessage(message = "Tìm kiếm user thành công!")
    public ResponseEntity<UserRes> fetchUserById(@PathVariable("id") long id) {
        UserRes user = this.userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping({"/user", "/admin/user"})
    @ApiMessage(message = "Cập nhật user thành công!")
    public ResponseEntity<UserRes> updateUser(@Valid @RequestBody UserReqUpdate userReqUpdate) throws CustomException {
        UserRes userRes = this.userService.ResUserUpdate(userReqUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(userRes);
    }

    @DeleteMapping("/admin/user/{id}")
    @ApiMessage(message = "Xóa user thành công!")
    public ResponseEntity<ResGlobal> deleteUser(@PathVariable("id") long id) throws CustomException {
        ResGlobal<Object> response = new ResGlobal<>();
        this.userService.deleteUser(id);
        return ResponseEntity.ok().body(response);
    }
}
