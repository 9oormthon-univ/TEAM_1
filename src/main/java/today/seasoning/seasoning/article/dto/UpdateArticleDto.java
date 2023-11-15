package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateArticleDto {

    @NotNull
    private Boolean isPublic;

    @NotNull
    private String contents;

    @NotNull
    private List<UpdateArticleImageDto> images;
}
