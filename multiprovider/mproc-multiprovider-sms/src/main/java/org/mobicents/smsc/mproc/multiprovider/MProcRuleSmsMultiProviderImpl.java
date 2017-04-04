/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2015, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package org.mobicents.smsc.mproc.multiprovider;

import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.tlv.TlvConvertException;
import com.cloudhopper.commons.util.ByteArrayUtil;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.smsc.mproc.MProcMessage;
import org.mobicents.smsc.mproc.MProcRuleBaseImpl;
import org.mobicents.smsc.mproc.MProcRuleRaProvider;
import org.mobicents.smsc.mproc.PostArrivalProcessor;

/**
 * 
 * @author sergey vetyutnev
 * @author abdul aziz ali
 * 
 */
public class MProcRuleSmsMultiProviderImpl extends MProcRuleBaseImpl {

    private static final int UNDEFINED_NETWORK = -1;
    //FIXME: should potentially be configurable
    private short tag = SmppConstants.TAG_DEST_NETWORK_ID;

    private int newNetworkId = UNDEFINED_NETWORK;

    @Override
    public String getRuleClassName() {
        return MProcRuleFactorySmsMultiProviderImpl.CLASS_NAME;
    }

    @Override
    public void setInitialRuleParameters(String parametersString) throws Exception {
        //FIXME: currently we are using hardcoded values    
    }

    @Override
    public void updateRuleParameters(String parametersString) throws Exception {
        //FIXME: currently we are using hardcoded values
    }

    @Override
    public String getRuleParameters() {
        //FIXME: currently we are using hardcoded values
        return null;
    }

    @Override
    public boolean isForPostArrivalState() {
        return true;
    }

    @Override
    public boolean isForPostDeliveryState() {
        return false;
    }	

    @Override
    public boolean matchesPostArrival(MProcMessage message) {
        //true if message contains Tlv we are interested in
        //FIXME: should we check message type here first? then save the tag value as an intsance prop?
        boolean res = false;
        if(message.getTlvSet()!=null) {
            try{
				//FIXME:returns null why? workaround with string first
                //newNetworkId = message.getTlvSet().getOptionalParameter(this.tag).getValueAsInt(); 
				Tlv tlv =  message.getTlvSet().getOptionalParameter(this.tag);				
				byte [] val = tlv.getValue();				
				int i0 = Integer.parseInt(tlv.getValueAsString());
				
				newNetworkId = i0;				
            }catch(Exception tce){
                //dont need to do anything 
                //TODO: add logging
				tce.printStackTrace();
            }
            if (newNetworkId!=-1){
                res = true;
            }
        }
        return res;
    }

    @Override
    public void onPostArrival(MProcRuleRaProvider anMProcRuleRa, PostArrivalProcessor factory, MProcMessage message)
            throws Exception {
        //TODO:check if correct message type, not receipt
        //FIXME: need to check second time if the tlv is present?

        if(this.newNetworkId != UNDEFINED_NETWORK) {
            //check if the tlv value is valid
            //TODO: do checks for the existence of the intended network id?
            //TODO: check if tag is within some allowable value range-pool?

            //update the new network id if all checks pass
            factory.updateMessageNetworkId(message, this.newNetworkId);
            //TODO:remove tlv from message?
            //factory.removeTlv(message, this.tag);
        }
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<MProcRuleSmsMultiProviderImpl> M_PROC_RULE_TEST_XML = new XMLFormat<MProcRuleSmsMultiProviderImpl>(
            MProcRuleSmsMultiProviderImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, MProcRuleSmsMultiProviderImpl mProcRule) throws XMLStreamException {
            //FIXME: currently we are using hardcoded values
        }

        @Override
        public void write(MProcRuleSmsMultiProviderImpl mProcRule, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            //FIXME: currently we are using hardcoded values
        }
    };
}
