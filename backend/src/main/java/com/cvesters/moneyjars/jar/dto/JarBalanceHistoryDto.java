package com.cvesters.moneyjars.jar.dto;

import java.util.List;
import java.util.Objects;

import lombok.Getter;

import com.cvesters.moneyjars.jar.bdo.JarBalanceHistory;

@Getter 
public class JarBalanceHistoryDto {
	private final long jarId;
	private final List<JarBalanceSnapshotDto> snapshots;

	public JarBalanceHistoryDto(final JarBalanceHistory bdo) {
		Objects.requireNonNull(bdo);

		this.jarId = bdo.getJarId();
		this.snapshots = bdo.getSnapshots()
				.entrySet()
				.stream()
				.map(JarBalanceSnapshotDto::new)
				.toList();
	}
}
