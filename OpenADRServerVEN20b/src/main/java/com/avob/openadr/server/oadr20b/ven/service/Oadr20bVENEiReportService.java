package com.avob.openadr.server.oadr20b.ven.service;

import javax.annotation.Resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.avob.openadr.model.oadr20b.Oadr20bJAXBContext;
import com.avob.openadr.model.oadr20b.builders.Oadr20bEiReportBuilders;
import com.avob.openadr.model.oadr20b.errorcodes.Oadr20bApplicationLayerErrorCode;
import com.avob.openadr.model.oadr20b.exception.Oadr20bApplicationLayerException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bMarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bUnmarshalException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureException;
import com.avob.openadr.model.oadr20b.exception.Oadr20bXMLSignatureValidationException;
import com.avob.openadr.model.oadr20b.oadr.OadrCancelReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCanceledReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrCreatedReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrPayload;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisterReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrRegisteredReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdateReportType;
import com.avob.openadr.model.oadr20b.oadr.OadrUpdatedReportType;
import com.avob.openadr.security.exception.OadrSecurityException;
import com.avob.openadr.server.oadr20b.ven.MultiVtnConfig;
import com.avob.openadr.server.oadr20b.ven.VtnSessionConfiguration;

@Service
public class Oadr20bVENEiReportService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Oadr20bVENEiReportService.class);

	protected static final String METADATA_REPORT_SPECIFIER_ID = "METADATA";
	protected static final String METADATA_REPORT_RID = "METADATA";
	protected static final String NO_GRANULARITY = "P0D";

	@Resource
	private Oadr20bJAXBContext jaxbContext;

	@Resource
	private XmlSignatureService xmlSignatureService;

	@Resource
	private MultiVtnConfig multiVtnConfig;

	public OadrRegisterReportType selfOadrRegisterReport(String requestId, String venId, String vtnSource,
			String reportRequestId) {
		return Oadr20bEiReportBuilders.newOadr20bRegisterReportBuilder(requestId, venId, reportRequestId).build();
	}

	public OadrUpdatedReportType oadrUpdateReport(VtnSessionConfiguration vtnConfig,
			OadrUpdateReportType oadrUpdateReportType) {

		String requestID = oadrUpdateReportType.getRequestID();
		String venID = oadrUpdateReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bUpdatedReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

	public OadrRegisteredReportType oadrRegisterReport(VtnSessionConfiguration vtnConfig,
			OadrRegisterReportType oadrRegisterReportType) {

		String requestID = oadrRegisterReportType.getRequestID();
		String venID = oadrRegisterReportType.getVenID();

		return Oadr20bEiReportBuilders.newOadr20bRegisteredReportBuilder(requestID, HttpStatus.OK_200, venID).build();

	}

	public OadrCreatedReportType oadrCreateReport(VtnSessionConfiguration vtnConfig,
			OadrCreateReportType oadrCreateReportType) {

		String requestID = oadrCreateReportType.getRequestID();
		String venID = oadrCreateReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bCreatedReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

	public OadrCanceledReportType oadrCancelReport(VtnSessionConfiguration vtnConfig,
			OadrCancelReportType oadrCancelReportType) {

		String requestID = oadrCancelReportType.getRequestID();
		String venID = oadrCancelReportType.getVenID();

		return Oadr20bEiReportBuilders
				.newOadr20bCanceledReportBuilder(requestID, Oadr20bApplicationLayerErrorCode.NOT_RECOGNIZED_453, venID)
				.build();
	}

	public String handle(VtnSessionConfiguration multiConfig, OadrUpdateReportType oadrUpdateReportType, boolean signed)
			throws Oadr20bXMLSignatureException, Oadr20bMarshalException, OadrSecurityException {

		OadrUpdatedReportType response = this.oadrUpdateReport(multiConfig, oadrUpdateReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, multiConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	public String handle(VtnSessionConfiguration multiConfig, OadrRegisterReportType oadrRegisterReportType,
			boolean signed) throws Oadr20bXMLSignatureException, Oadr20bMarshalException, OadrSecurityException {

		OadrRegisteredReportType response = this.oadrRegisterReport(multiConfig, oadrRegisterReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, multiConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	public String handle(VtnSessionConfiguration multiConfig, OadrCreateReportType oadrCreateReportType, boolean signed)
			throws Oadr20bXMLSignatureException, Oadr20bMarshalException, OadrSecurityException {

		OadrCreatedReportType response = this.oadrCreateReport(multiConfig, oadrCreateReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, multiConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	public String handle(VtnSessionConfiguration multiConfig, OadrCancelReportType oadrCancelReportType, boolean signed)
			throws Oadr20bXMLSignatureException, Oadr20bMarshalException, OadrSecurityException {

		OadrCanceledReportType response = this.oadrCancelReport(multiConfig, oadrCancelReportType);

		String responseStr = null;

		if (signed) {
			responseStr = xmlSignatureService.sign(response, multiConfig);
		} else {
			responseStr = jaxbContext.marshalRoot(response);
		}

		return responseStr;
	}

	public String handle(VtnSessionConfiguration multiConfig, String raw, OadrPayload oadrPayload)
			throws Oadr20bXMLSignatureValidationException, Oadr20bMarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureException, OadrSecurityException {
		xmlSignatureService.validate(raw, oadrPayload, multiConfig);

		if (oadrPayload.getOadrSignedObject().getOadrUpdateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrUpdateReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrUpdateReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrCreateReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCreateReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrCreateReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrRegisterReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrRegisterReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrRegisterReport(), true);
		} else if (oadrPayload.getOadrSignedObject().getOadrCancelReport() != null) {
			LOGGER.info(multiConfig.getVtnId() + " - OadrCancelReport signed");
			return handle(multiConfig, oadrPayload.getOadrSignedObject().getOadrCancelReport(), true);
		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

	public String request(String username, String payload)
			throws Oadr20bMarshalException, Oadr20bUnmarshalException, Oadr20bApplicationLayerException,
			Oadr20bXMLSignatureValidationException, Oadr20bXMLSignatureException, OadrSecurityException {

		Object unmarshal = jaxbContext.unmarshal(payload);

		VtnSessionConfiguration multiConfig = multiVtnConfig.getMultiConfig(username);

		if (unmarshal instanceof OadrPayload) {

			OadrPayload oadrPayload = (OadrPayload) unmarshal;

			return handle(multiConfig, payload, oadrPayload);

		} else if (unmarshal instanceof OadrCancelReportType) {

			LOGGER.debug(payload);

			OadrCancelReportType oadrCancelReportType = (OadrCancelReportType) unmarshal;

			LOGGER.info(username + " - OadrCancelReport");

			return handle(multiConfig, oadrCancelReportType, false);

		} else if (unmarshal instanceof OadrCreateReportType) {

			LOGGER.debug(payload);

			OadrCreateReportType oadrCreateReportType = (OadrCreateReportType) unmarshal;

			LOGGER.info(username + " - OadrCreateReport");

			return handle(multiConfig, oadrCreateReportType, false);

		} else if (unmarshal instanceof OadrRegisterReportType) {

			LOGGER.debug(payload);

			OadrRegisterReportType oadrRegisterReportType = (OadrRegisterReportType) unmarshal;

			LOGGER.info(username + " - OadrRegisterReport");

			return handle(multiConfig, oadrRegisterReportType, false);

		} else if (unmarshal instanceof OadrUpdateReportType) {

			LOGGER.debug(payload);

			OadrUpdateReportType oadrUpdateReportType = (OadrUpdateReportType) unmarshal;

			LOGGER.info(username + " - OadrUpdateReport");

			return handle(multiConfig, oadrUpdateReportType, false);

		}

		throw new Oadr20bApplicationLayerException("Unacceptable request payload for EiReport");
	}

}
