package com.coremedia.blueprint.studio.feedbackhub.siteimprove.components {
import com.coremedia.blueprint.studio.feedbackhub.siteimprove.ScoreUtil;
import com.coremedia.cms.studio.feedbackhub.model.FeedbackItem;
import com.coremedia.ui.data.PropertyPathExpression;
import com.coremedia.ui.data.ValueExpression;
import com.coremedia.ui.data.ValueExpressionFactory;

import ext.Ext;

import ext.container.Container;

[ResourceBundle('com.coremedia.blueprint.studio.feedbackhub.siteimprove.FeedbackHubSiteimprove')]
public class ScoreCircleBase extends Container {

  public static const SCORE_ITEM_ID:String = "scoreItem";

  [Bindable]
  public var bindTo:ValueExpression;

  [Bindable]
  public var crawlDateExpression:ValueExpression;

  [Bindable]
  public var color:String;

  [Bindable]
  public var label:String;

  [Bindable]
  public var showDiff:Boolean;

  public function ScoreCircleBase(config:ScoreCircle = null) {
    super(config);
  }

  override protected function afterRender():void {
    super.afterRender();
    renderScore();
  }

  private function renderScore():void {
    var el = queryById(SCORE_ITEM_ID).el;
    var score:Number = bindTo.getValue();
    var options = {
      percent: el.getAttribute('data-percent') || ScoreUtil.formatScore(score),
      size: el.getAttribute('data-size') || 170,
      lineWidth: el.getAttribute('data-line') || 12,
      rotate: el.getAttribute('data-rotate') || 0
    };

    var canvas = window.document.createElement('canvas');
    canvas.setAttribute("style", "top:0;left:0;margin-left: -85px;");

    var div = window.document.createElement('div');
    div.setAttribute('style', 'width: 100%;text-align: center;padding-left:85px;');

    var spanScore = window.document.createElement('span');
    spanScore.setAttribute("style", " color:black;display:inline;font-family:sans-serif;position:absolute;margin-left: -66px;top:100px;font-size:44px;font-weight:bold;");
    spanScore.textContent = options.percent;

    var span100 = window.document.createElement('span');
    span100.setAttribute("style", " color:#b1b1b1;display:inline;font-family:sans-serif;position:absolute; top: 106px;margin-left:20px;font-size:24px;");
    span100.textContent = '/100';

    var ctx = canvas.getContext('2d');
    canvas.width = canvas.height = options.size;

    div.appendChild(spanScore);
    div.appendChild(span100);

    div.appendChild(canvas);
    el.appendChild(div);

    if (showDiff) {
      //render the diff only if there is the last value
      if (ScoreUtil.getLastExpression(bindTo).getValue()) {
        var spanDiff = window.document.createElement('span');
        spanDiff.setAttribute("style", " color:black;display:inline;font-family:sans-serif;position:absolute; top: 136px;margin-left: -130px;font-size:22px;");
        div.appendChild(spanDiff);
        var scoreDiffConfig:ScoreDiff = ScoreDiff({});
        scoreDiffConfig.bindToValue1 = bindTo;
        scoreDiffConfig.bindToValue2 = ScoreUtil.getLastExpression(bindTo);
        scoreDiffConfig.renderTo = spanDiff;
        Ext.create(ScoreDiff, scoreDiffConfig);
      }
    }

    ctx.translate(options.size / 2, options.size / 2); // change center
    ctx.rotate((-1 / 2 + options.rotate / 180) * Math.PI); // rotate -90 deg

    var radius = (options.size - options.lineWidth) / 2;
    var drawCircle = function (color, lineWidth, percent) {
      percent = Math.min(Math.max(0, percent || 1), 1);
      ctx.beginPath();
      ctx.arc(0, 0, radius, 0, Math.PI * 2 * percent, false);
      ctx.strokeStyle = color;
      ctx.lineCap = 'butt';
      ctx.lineWidth = lineWidth;
      ctx.stroke();
    };

    drawCircle('#efefef', options.lineWidth, 100 / 100);
    drawCircle(color || ScoreUtil.getColor(score), options.lineWidth, options.percent / 100);
  }
}
}
