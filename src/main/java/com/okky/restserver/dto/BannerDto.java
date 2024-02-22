package com.okky.restserver.dto;

import java.util.Date;

import com.okky.restserver.domain.Banner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "배너DTO")
@Setter
@Getter
@NoArgsConstructor
public class BannerDto {
	
	@Schema(description = "이름", nullable = false)
	@NotNull
	private String name;
	
	@Schema(description = "타입", nullable = false)
	@NotNull
	private String type;
	
	@Schema(description = "URL", nullable = false)
	@NotNull
	private String url;
	
	@Schema(description = "생성일시", nullable = false)
	@NotNull
	private Date dateCreated;
	
	@Builder
	public BannerDto(String name, String type, String url, Date dateCreated) {
		this.name = name;
		this.type = type;
		this.url = url;
		this.dateCreated = dateCreated;
	}
	
	public static BannerDto convertToDto(Banner banner) {
		return BannerDto.builder()
					.name(banner.getName())
					.type(banner.getType())
					.url(banner.getUrl())
					.dateCreated(banner.getDateCreated())
					.build();
	}
	
}
