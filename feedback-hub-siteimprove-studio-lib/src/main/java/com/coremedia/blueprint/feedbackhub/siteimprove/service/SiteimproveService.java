package com.coremedia.blueprint.feedbackhub.siteimprove.service;

import com.coremedia.blueprint.feedbackhub.siteimprove.SiteimproveSettings;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.AccessibilityIssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.AnalyticsSummaryDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.BrokenLinkPagesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.ContentCheckIssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.ContentCheckResultDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.CrawlStatusDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.DciOverallScoreDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.PageCheckResultDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.PageCheckStatusDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.PageDetailsDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.PageDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.PagesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.QualitySummaryDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.SeoIssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.Seov2IssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.SiteDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.CrawlResultDocument;
import com.coremedia.cap.content.Content;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.util.MultiValueMap;

public interface SiteimproveService {

  /**
   * Trigger a crawl request for a site.
   * Note that this request triggers the whole site crawl which is very expensive
   * @param config
   * @param siteId
   * @return
   */
  @Nullable
  CrawlResultDocument crawl(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  CrawlStatusDocument getCrawlStatus(@NonNull SiteimproveSettings config, @NonNull String siteId);

  /**
   * Trigger a page check
   * Note that the page check can take up to 30 seconds
   * @param config
   * @param content
   * @param pageId if not given the check will be done against the url of the content
   * @return
   */
  @Nullable
  PageCheckResultDocument pageCheck(@NonNull SiteimproveSettings config, @NonNull Boolean preview, @NonNull Content content, @Nullable String pageId);

  /**
   * Get the status of page check
   * Note that the page check can take up to 30 seconds
   * @param config
   * @param content
   * @param pageId if not given the check will be done against the url of the content
   * @return
   */
  @Nullable
  PageCheckStatusDocument getPageCheckStatus(@NonNull SiteimproveSettings config, @NonNull Boolean preview, @NonNull Content content, @Nullable String pageId);

  @Nullable
  ContentCheckResultDocument contentCheck(@NonNull SiteimproveSettings config, @NonNull String body);

  @Nullable
  ContentCheckIssuesDocument getContentCheckIssues(@NonNull SiteimproveSettings config, @NonNull String contentId);

  @Nullable
  default DciOverallScoreDocument getDCIScore(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    return getDCIScore(config, siteId, null);
  }

  @Nullable
  DciOverallScoreDocument getDCIScore(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable String pageId);

  @Nullable
  QualitySummaryDocument getQualitySummary(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  PagesDocument getMisspellingPages(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  PagesDocument getMisspellingPages(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable MultiValueMap<String, String> queryParamContentID);

  @Nullable
  SeoIssuesDocument getSeoIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  SeoIssuesDocument getSeoIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId);

  @Nullable
  Seov2IssuesDocument getSeov2IssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId);

  @Nullable
  AccessibilityIssuesDocument getAccessibilityIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId);

  @Nullable
  PageDetailsDocument getPageDetails(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId);

  @Nullable
  BrokenLinkPagesDocument getBrokenLinkPages(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  BrokenLinkPagesDocument getBrokenLinkPages(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable MultiValueMap<String, String> queryParamContentID);

  @Nullable
  AnalyticsSummaryDocument getAnalyticsSummary(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  SiteDocument getSite(@NonNull SiteimproveSettings config, @NonNull String siteId);

  @Nullable
  PageDocument findPage(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull Content content);

  @Nullable
  PageDocument findPageByUrl(@NonNull SiteimproveSettings config, @NonNull Content content, Boolean preview);

}
