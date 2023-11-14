package today.seasoning.seasoning.article.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import today.seasoning.seasoning.common.util.TsidUtil;

@Entity
@NoArgsConstructor
public class ArticleImage {

	@Id
	private Long id;

	@Check(constraints = "sequence >= 1")
	@Column(nullable = false)
	private int sequence;

	@Column(nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	public ArticleImage(int sequence, String url, Article article) {
		this.id = TsidUtil.createLong();
		this.sequence = sequence;
		this.url = url;
		this.article = article;
	}
}
