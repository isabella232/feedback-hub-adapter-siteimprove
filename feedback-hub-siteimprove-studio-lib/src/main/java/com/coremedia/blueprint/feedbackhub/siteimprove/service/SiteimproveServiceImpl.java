package com.coremedia.blueprint.feedbackhub.siteimprove.service;

import com.coremedia.blueprint.feedbackhub.siteimprove.SiteimproveSettings;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.AccessibilityIssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.AnalyticsSummaryDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.BrokenLinkPagesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.ContentCheckIssuesDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.ContentCheckResultDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.CrawlStatusDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.DciOverallScoreDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.MetatagDocument;
import com.coremedia.blueprint.feedbackhub.siteimprove.service.documents.MetatagsDocument;
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
import com.coremedia.cap.common.IdHelper;
import com.coremedia.cap.content.Content;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class SiteimproveServiceImpl implements SiteimproveService {

  private static final String SITES = "/sites/";
  private SiteimproveRestConnector connector;

  SiteimproveServiceImpl(SiteimproveRestConnector siteimproveRestConnector) {
    this.connector = siteimproveRestConnector;
  }

  @Nullable
  @Override
  public CrawlResultDocument crawl(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId + "/content/crawl";
    return connector.performPost(config, resourcePath, CrawlResultDocument.class, null);
  }

  @Override
  @Nullable
  public CrawlStatusDocument getCrawlStatus(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId + "/content/crawl";
    return connector.performGet(config, resourcePath, CrawlStatusDocument.class, null);
  }

  @Nullable
  @Override
  public PageCheckResultDocument pageCheck(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    String resourcePath = SITES + siteId + "/content/check/page/" + pageId;
    return connector.performPost(config, resourcePath, PageCheckResultDocument.class, null);
  }

  @Nullable
  @Override
  public PageCheckStatusDocument getPageCheckStatus(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    String resourcePath = SITES + siteId + "/content/check/page/" + pageId;
    return connector.performGet(config, resourcePath, PageCheckStatusDocument.class, null);
  }

  @Nullable
  @Override
  public ContentCheckResultDocument contentCheck(@NonNull SiteimproveSettings config, @NonNull String body) {
    return connector.performPost(config, "/content/check", ContentCheckResultDocument.class, null, body);
  }

  @Nullable
  @Override
  public ContentCheckIssuesDocument getContentCheckIssues(@NonNull SiteimproveSettings config, @NonNull String contentId) {
    String resourcePath = "/content/checks/" + contentId + "/issues";
    ContentCheckIssuesDocument contentCheckIssuesDocument = connector.performGet(config, resourcePath, ContentCheckIssuesDocument.class, null);
    String message = contentCheckIssuesDocument.getMessage();
    if (StringUtils.isEmpty(message)) {
      return contentCheckIssuesDocument;
    }

    return null;
  }

  @Override
  @Nullable
  public DciOverallScoreDocument getDCIScore(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable String pageId) {
    String resourcePath = SITES + siteId + "/dci/overview";
    MultiValueMap<String, String> queryParams = null;
    if (pageId != null) {
      queryParams = new LinkedMultiValueMap<>();
      queryParams.add("page_id", pageId);
    }
    return connector.performGet(config, resourcePath, DciOverallScoreDocument.class, queryParams);
  }

  @Override
  @Nullable
  public QualitySummaryDocument getQualitySummary(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId + "/quality_assurance/overview/summary";
    QualitySummaryDocument qualitySummary = connector.performGet(config, resourcePath, QualitySummaryDocument.class, null);

    if (qualitySummary == null) {
      return null;
    }

    resourcePath = SITES + siteId + "/seo/issues";
    SeoIssuesDocument seoIssuesDocument = connector.performGet(config, resourcePath, SeoIssuesDocument.class, null);
    if (seoIssuesDocument != null) {
      qualitySummary.setSeoIssues(seoIssuesDocument.getTotalItems());
    }

    return qualitySummary;
  }

  @Override
  @Nullable
  public PagesDocument getMisspellingPages(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    return getMisspellingPages(config, siteId, null);
  }

  @Nullable
  @Override
  public PagesDocument getMisspellingPages(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable MultiValueMap<String, String> queryParamContentID) {
    String resourcePath = SITES + siteId + "/quality_assurance/spelling/pages";
    return connector.performGet(config, resourcePath, PagesDocument.class, queryParamContentID);
  }

  @Override
  @Nullable
  public SeoIssuesDocument getSeoIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId + "/seo/issues?page=1&page_size=100";
    return connector.performGet(config, resourcePath, SeoIssuesDocument.class, null);
  }

  @Nullable
  @Override
  public SeoIssuesDocument getSeoIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    //check SEO
    String seoPageIssuesUrl = SITES + siteId + "/seo/pages/" + pageId + "/issues";
    return connector.performGet(config, seoPageIssuesUrl, SeoIssuesDocument.class, null);
  }

  @Nullable
  @Override
  public Seov2IssuesDocument getSeov2IssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    String seov2PageIssuesUrl = SITES + siteId + "/seov2/pages/" + pageId + "/issues";
    Seov2IssuesDocument issues = connector.performGet(config, seov2PageIssuesUrl, Seov2IssuesDocument.class, null);

    //paging
    Seov2IssuesDocument nextIssues = issues;
    while (nextIssues.hasNext()) {
      nextIssues = connector.performGet(config, nextIssues.nextUrl(), Seov2IssuesDocument.class, null);
      issues.add(nextIssues);
    }
    return issues;
  }

  @Nullable
  @Override
  public AccessibilityIssuesDocument getAccessibilityIssuePages(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    String seov2PageIssuesUrl = SITES + siteId + "/accessibility/pages/" + pageId + "/issues";
    AccessibilityIssuesDocument issues = connector.performGet(config, seov2PageIssuesUrl, AccessibilityIssuesDocument.class, null);

    //paging
    AccessibilityIssuesDocument nextIssues = issues;
    while (nextIssues.hasNext()) {
      nextIssues = connector.performGet(config, nextIssues.nextUrl(), AccessibilityIssuesDocument.class, null);
      issues.add(nextIssues);
    }
    return issues;
  }

  @Nullable
  @Override
  public PageDetailsDocument getPageDetails(@NonNull SiteimproveSettings config, @NonNull String siteId, @NonNull String pageId) {
    String pageDetailsUrl = SITES + siteId + "/content/pages/" + pageId;
    return connector.performGet(config, pageDetailsUrl, PageDetailsDocument.class, null);
  }

  @Nullable
  @Override
  public BrokenLinkPagesDocument getBrokenLinkPages(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    return getBrokenLinkPages(config, siteId, null);
  }

  @Nullable
  @Override
  public BrokenLinkPagesDocument getBrokenLinkPages(@NonNull SiteimproveSettings config, @NonNull String siteId, @Nullable MultiValueMap<String, String> queryParamContentID) {
    String resourcePath = SITES + siteId + "/quality_assurance/links/pages_with_broken_links";
    return connector.performGet(config, resourcePath, BrokenLinkPagesDocument.class, null);
  }

  @Nullable
  @Override
  public AnalyticsSummaryDocument getAnalyticsSummary(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId + "/analytics/overview/summary";
    return connector.performGet(config, resourcePath, AnalyticsSummaryDocument.class, null);
  }

  @Nullable
  @Override
  public SiteDocument getSite(@NonNull SiteimproveSettings config, @NonNull String siteId) {
    String resourcePath = SITES + siteId;
    MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
    return connector.performGet(config, resourcePath, SiteDocument.class, queryParam);
  }

  @Nullable
  @Override
  public PageDocument findPage(@NonNull SiteimproveSettings config,
                               @NonNull String siteId,
                               @NonNull Content content) {

    MultiValueMap<String, String> contentMetatagQueryParam = new LinkedMultiValueMap<>();
    contentMetatagQueryParam.add("query", createMetaTag(content));
    String metaTagsResourcePath = SITES + siteId + "/quality_assurance/inventory/meta_tags";
    MetatagsDocument metatagsDocument = connector.performGet(config, metaTagsResourcePath, MetatagsDocument.class, contentMetatagQueryParam);

    if (metatagsDocument == null || metatagsDocument.getItems().isEmpty()) {
      return null;
    }

    MetatagDocument metatagDocument = metatagsDocument.getItems().get(0);
    metaTagsResourcePath = metaTagsResourcePath + "/" + metatagDocument.getId() + "/contents";
    metatagsDocument = connector.performGet(config, metaTagsResourcePath, MetatagsDocument.class, null);

    if (metatagsDocument == null || metatagsDocument.getItems().isEmpty()) {
      return null;
    }

    metatagDocument = metatagsDocument.getItems().get(0);
    metaTagsResourcePath += "/" + metatagDocument.getId() + "/pages";
    PagesDocument pagesDocument = connector.performGet(config, metaTagsResourcePath, PagesDocument.class, null);

    if (pagesDocument == null || pagesDocument.getPages().isEmpty()) {
      return null;
    }

    return pagesDocument.getPages().get(0);
  }

  /**
   * This means that you can now add your meta-tags with the id properly in the content-attribute, not tacked onto the tag-name itself. Something like this:
   *
   * <meta name="coremedia:content-id" content="xyz123_the_id_goes_here">
   * <p>
   * And then find these exact pages by searching for content for that tag matching exactly the desired id. The search, like for the meta-tag name itself, is case-insensitive.
   * The query-parameter for doing that search will be like what is used for e.g. the meta-tags themselves, namely “query= xyz123_the_id_goes_here“.
   * <p>
   * The documentation for the endpoint will be updated once deployed:
   * https://api.siteimprove.com/v2/documentation#!/Quality_Assurance/get_sites_site_id_quality_assurance_inventory_meta_tags_meta_name_id_contents
   */
  private String createMetaTag(@NonNull Content content) {
    return String.valueOf(IdHelper.parseContentId(content.getId()));
  }

}
