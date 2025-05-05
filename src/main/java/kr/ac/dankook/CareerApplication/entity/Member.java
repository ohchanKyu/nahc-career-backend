package kr.ac.dankook.CareerApplication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String email;
    private String name;
    private String roles;
    private int downloadCount = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<LLMChatSection> chatSections = new ArrayList<>();

    public List<String> getRoleList(){
        if (!this.roles.isEmpty()){
            return Arrays.asList(this.roles.split(","));
        }
        return List.of();
    }
}
