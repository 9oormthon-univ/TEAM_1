package today.seasoning.seasoning.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateArticleImageDto {

    @NotNull
    private Boolean updated;

    @NotNull
    private String id;

    @Min(1)
    @NotNull
    private Integer sequence;

    @NotBlank
    private String url;
}
