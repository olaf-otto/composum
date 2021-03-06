<%@page session="false" pageEncoding="utf-8"%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.2"%>
<%@taglib prefix="cpn" uri="http://sling.composum.com/cpnl/1.0"%>
<sling:defineObjects />
<cpn:component id="browser" type="com.composum.sling.core.browser.Browser" scope="request">
  <div class="json detail-panel">
    <div class="json-toolbar detail-toolbar">
      <div class="btn-group btn-group-sm" role="group">
        <div class="depth input-group input-group-sm widget number-field-widget">
          <span class="fa fa-level-down input-group-addon text-muted" title="content depth"></span>
          <input type="text" class="form-control" />
          <span class="input-group-addon spinner">
              <span class="decrement fa fa-minus" title="decrement"></span>
              <span class="increment fa fa-plus" title="increment"></span>
          </span>
        </div>
      </div>
      <div class="binary btn-group btn-group-sm widget select-buttons-widget">
        <button type="button" data-value="link" class="fa fa-external-link btn btn-default" title="embed link to binary data"><span class="label">Link</span></button>
        <button type="button" data-value="base64" class="fa fa-plus-square btn btn-default" title="embed binary data base64 encoded"><span class="label">Base 64</span></button>
        <button type="button" data-value="skip" class="fa fa-minus-square-o btn btn-default" title="skip binary data"><span class="label">Skip</span></button>
      </div>
      <div class="btn-group btn-group-sm" role="group">
        <div class="indent input-group input-group-sm widget number-field-widget" data-options="0:2:4">
          <span class="fa fa-indent input-group-addon text-muted" title="text indent"></span>
          <input type="text" class="form-control" />
          <span class="input-group-addon spinner">
              <span class="decrement fa fa-minus" title="decrement"></span>
              <span class="increment fa fa-plus" title="increment"></span>
          </span>
        </div>
      </div>
      <div class="btn-group btn-group-sm" role="group">
        <button type="button" class="reload fa fa-refresh btn btn-default" title="Reload JSON view"><span class="label">Reload</span></button>
      </div>
      <div class="btn-group btn-group-sm" role="group">
        <a href="" class="download fa fa-download btn btn-default" title="Download as JSON file"><span class="label">Download</span></a>
        <button type="button" class="upload fa fa-upload btn btn-default" title="Update from an uploaded JSON file"><span class="label">Upload</span></button>
      </div>
    </div>
    <div class="embedded frame-container detail-content">
      <iframe src="" width="100%" height="100%"></iframe>
    </div>
  </div>
</cpn:component>