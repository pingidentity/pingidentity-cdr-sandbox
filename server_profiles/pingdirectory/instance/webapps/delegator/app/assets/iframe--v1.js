/**
 * @file This file is kept intact for reference only. Please use the transpiled and minified version
 * for your custom attributes.
 */

(function customAttributesIframe() {
  const RECEIVED_MESSAGE_ACTIONS = {
    INITIALIZE_ATTRIBUTE: 'INITIALIZE_ATTRIBUTE',
    SET_ATTRIBUTE_DATA: 'SET_ATTRIBUTE_DATA',
    FORM_UPDATE: 'FORM_UPDATE',
  };

  const SENT_MESSAGE_ACTIONS = {
    IFRAME_LOADED: 'IFRAME_LOADED',
    IFRAME_INITIALIZED: 'IFRAME_INITIALIZED',
    SET_VALUE: 'SET_VALUE',
    SET_IS_VALID: 'SET_IS_VALID',
    RESIZE_IFRAME: 'RESIZE_IFRAME',
  };
  class CustomAttribute {
    constructor() {
      this.fileVersion = 'v1';
      this.attribute = undefined;
      this.origin = '*';
      this.value = undefined;
      this.metaData = undefined;
      this.formData = undefined;
    }

    sendMessage = (msg) => {
      const { parent } = window;
      const message = { ...msg, fileVersion: this.fileVersion };
      parent.postMessage(message, this.origin);
    }

    receiveMessage = (e) => {
      const message = e.data;
      const { data } = message;
      if (message.action === RECEIVED_MESSAGE_ACTIONS.INITIALIZE_ATTRIBUTE) {
        this.attribute = data.attr;
        this.origin = data.origin;
        this.iframeInitialized();
      } else if (message.action === RECEIVED_MESSAGE_ACTIONS.SET_ATTRIBUTE_DATA) {
        this.value = data.value;
        this.metaData = data.metaData;
        if (typeof this.initializeHook === 'function') this.initializeHook(data);
      } else if (message.action === RECEIVED_MESSAGE_ACTIONS.FORM_UPDATE) {
        this.formData = data.form.attributes;
        if (typeof this.updateHook === 'function') this.updateHook(data);
      }
    }

    iframeInitialized = () => {
      this.sendMessage({
        action: SENT_MESSAGE_ACTIONS.IFRAME_INITIALIZED,
        attribute: this.attribute,
      });
    }

    /**
     * Tell the form what the value of the custom attribute is so that it sends this information
     * to the server upon save.
     * @param {[] | boolean | number | {} | string} value The custom attribute's value
     */
    sendAttributeValue = (value) => {
      this.sendMessage({
        action: SENT_MESSAGE_ACTIONS.SET_VALUE,
        data: value,
        attribute: this.attribute,
      });
    }

    /**
     * Tell the form whether the custom attribute is valid. If not valid, this will prevent the form
     * from enabling the save button. By default, all custom attributes are valid to begin with.
     * @param {boolean} isValid Whether the custom attribute is valid
     */
    sendAttributeValidation = (isValid) => {
      this.sendMessage({
        action: SENT_MESSAGE_ACTIONS.SET_IS_VALID,
        data: isValid,
        attribute: this.attribute,
      });
    }

    /**
     * Tell the form to resize the bounding iFrame for the custom attribute.
     * @param {Number} [pixels] The number of pixels of height the bounding iFrame should be.
     *   If not passed in, the formula used to determine the new height is the following:
     *   Math.max(body.clientHeight,
     *     body.offsetHeight,
     *     body.scrollHeight,
     *     documentElement.clientHeight,
     *     documentElement.offsetHeight,
     *     documentElement.scrollHeight);
     */
    resize = (pixels) => {
      this.sendMessage({
        action: SENT_MESSAGE_ACTIONS.RESIZE_IFRAME,
        attribute: this.attribute,
        data: pixels,
      });
    }

    /**
     * Define a function to be called when the custom attribute first gets information sent to it
     * from the form.
     * @param {Function} callback The function which will be called after form initialization
     */
    onInitialize = (callback) => {
      this.initializeHook = callback;
    }

    /**
     * Define a function to be Called when the custom attribute gets new information sent to it
     * from the form. This is typically as a result of changes to other attributes on the form so
     * that this custom attribute may react to those changes.
     * @param {Function} callback The function which will be called after form updates
     */
    onUpdate = (callback) => {
      this.updateHook = callback;
    }
  }

  const customAttr = new CustomAttribute();
  window.CustomAttribute = customAttr;

  window.addEventListener('message', (e) => {
    customAttr.receiveMessage(e);
  });

  document.addEventListener('DOMContentLoaded', () => {
    customAttr.sendMessage({
      action: SENT_MESSAGE_ACTIONS.IFRAME_LOADED,
    });
  });
}());
