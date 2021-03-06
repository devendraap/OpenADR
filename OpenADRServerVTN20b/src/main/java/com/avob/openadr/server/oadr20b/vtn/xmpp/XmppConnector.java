package com.avob.openadr.server.oadr20b.vtn.xmpp;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.avob.openadr.client.xmpp.oadr20b.OadrXmppClient20b;
import com.avob.openadr.client.xmpp.oadr20b.OadrXmppException;
import com.avob.openadr.server.common.vtn.VtnConfig;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiEventService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiOptService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiRegisterPartyService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiReportService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNEiService;
import com.avob.openadr.server.oadr20b.vtn.service.ei.Oadr20bVTNPayloadService;

@Service
public class XmppConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmppConnector.class);

	@Resource
	private VtnConfig vtnConfig;

	@Resource
	private Oadr20bVTNPayloadService oadr20bVTNPayloadService;

	@Resource
	private Oadr20bVTNEiRegisterPartyService oadr20bVTNEiRegisterPartyService;

	@Resource
	private Oadr20bVTNEiReportService oadr20bVTNEiReportService;

	@Resource
	private Oadr20bVTNEiEventService oadr20bVTNEiEventService;

	@Resource
	private Oadr20bVTNEiOptService oadr20bVTNEiOptService;

	private OadrXmppClient20b xmppUplinkClient;

	private XMPPTCPConnection getXmppConnection(String domain, Oadr20bVTNEiService service) throws OadrXmppException {
		String resource = (service != null) ? service.getServiceName() : "uplink";
		SSLContext sslContext = vtnConfig.getSslContext();
		String host = vtnConfig.getXmppHost();
		int port = vtnConfig.getXmppPort();

		XMPPTCPConnectionConfiguration anonymousConnection = OadrXmppClient20b.anonymousConnection(host, port, domain,
				resource, sslContext);
		return new XMPPTCPConnection(anonymousConnection);
	}

	private OadrXmppClient20b getXmppClient(String domain, Oadr20bVTNEiService service, OadrXmppClient20b uplinkClient)
			throws OadrXmppException {
		String resource = (service != null) ? service.getServiceName() : "uplink";
		XmppListener xmppListener = new XmppListener(service, uplinkClient, oadr20bVTNPayloadService);
		XMPPTCPConnection xmppConnection = getXmppConnection(domain, service);
		String jid = vtnConfig.getVtnId() + "@" + domain + "/" + resource;
		return new OadrXmppClient20b(jid, xmppConnection, domain, xmppListener);
	}

	private OadrXmppClient20b getXmppUplinkClient(String domain) throws OadrXmppException {
		XMPPTCPConnection xmppConnection = getXmppConnection(domain, null);
		String jid = vtnConfig.getVtnId() + "@" + domain + "/uplink";
		return new OadrXmppClient20b(jid, xmppConnection, domain, null);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws OadrXmppException {

		if (vtnConfig.getXmppHost() != null && vtnConfig.getXmppPort() != null) {

			String domain = (vtnConfig.getXmppDomain() != null) ? vtnConfig.getXmppDomain() : vtnConfig.getXmppHost();

			setXmppUplinkClient(getXmppUplinkClient(domain));

			LOGGER.info("Xmpp VTN uplink client successfully initialized");

			getXmppClient(domain, oadr20bVTNEiReportService, getXmppUplinkClient());

			getXmppClient(domain, oadr20bVTNEiRegisterPartyService, getXmppUplinkClient());
			getXmppClient(domain, oadr20bVTNEiOptService, getXmppUplinkClient());
			getXmppClient(domain, oadr20bVTNEiEventService, getXmppUplinkClient());

			LOGGER.info("Xmpp VTN connectors successfully initialized");

		}

	}

	public OadrXmppClient20b getXmppUplinkClient() {
		return xmppUplinkClient;
	}

	public void setXmppUplinkClient(OadrXmppClient20b xmppUplinkClient) {
		this.xmppUplinkClient = xmppUplinkClient;
	}

}
