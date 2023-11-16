package today.seasoning.seasoning.article.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import today.seasoning.seasoning.common.util.TsidUtil;

@Entity
@Getter
@NoArgsConstructor
public class ArticleImage {

	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	@Column(nullable = false)
	private String url;

	@Check(constraints = "sequence >= 1")
	@Column(nullable = false)
	private int sequence;

	public ArticleImage(Article article, String url, int sequence) {
		this.id = TsidUtil.createLong();
		this.article = article;
		this.url = url;
		this.sequence = sequence;
	}

	public ArticleImage(Long id, Article article, String url, int sequence) {
		this.id = id;
		this.article = article;
		this.url = url;
		this.sequence = sequence;
	}
}
