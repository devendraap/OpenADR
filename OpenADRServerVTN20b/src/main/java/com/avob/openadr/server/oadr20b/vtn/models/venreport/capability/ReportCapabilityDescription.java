package com.avob.openadr.server.oadr20b.vtn.models.venreport.capability;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

import com.avob.openadr.model.oadr20b.ei.ReadingTypeEnumeratedType;
import com.avob.openadr.model.oadr20b.ei.ReportEnumeratedType;
import com.avob.openadr.model.oadr20b.siscale.SiScaleCodeType;

/**
 * @author bzanni
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ReportCapabilityDescription {

	/**
	 * Autogenerated unique id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	private String rid;

	@Enumerated(EnumType.STRING)
	private ReportEnumeratedType reportType;

	@Enumerated(EnumType.STRING)
	private ReadingTypeEnumeratedType readingType;

	private String oadrMaxPeriod;

	private String oadrMinPeriod;

	private boolean oadrOnChange;

	private String itemDescription;

	private String itemUnits;

	@Enumerated(EnumType.STRING)
	private SiScaleCodeType siScaleCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public ReportEnumeratedType getReportType() {
		return reportType;
	}

	public void setReportType(ReportEnumeratedType reportType) {
		this.reportType = reportType;
	}

	public ReadingTypeEnumeratedType getReadingType() {
		return readingType;
	}

	public void setReadingType(ReadingTypeEnumeratedType readingType) {
		this.readingType = readingType;
	}

	public String getOadrMaxPeriod() {
		return oadrMaxPeriod;
	}

	public void setOadrMaxPeriod(String oadrMaxPeriod) {
		this.oadrMaxPeriod = oadrMaxPeriod;
	}

	public String getOadrMinPeriod() {
		return oadrMinPeriod;
	}

	public void setOadrMinPeriod(String oadrMinPeriod) {
		this.oadrMinPeriod = oadrMinPeriod;
	}

	public boolean isOadrOnChange() {
		return oadrOnChange;
	}

	public void setOadrOnChange(boolean oadrOnChange) {
		this.oadrOnChange = oadrOnChange;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemUnits() {
		return itemUnits;
	}

	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}

	public SiScaleCodeType getSiScaleCode() {
		return siScaleCode;
	}

	public void setSiScaleCode(SiScaleCodeType siScaleCode) {
		this.siScaleCode = siScaleCode;
	}
}
