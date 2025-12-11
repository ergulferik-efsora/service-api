package com.epam.reportportal.util;

import static com.epam.reportportal.serviceapi.infrastructure.persistence.util.MembershipUtils.rpUserToMembership;

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.ReportPortalUser;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ReportPortalException;
import com.epam.reportportal.serviceapi.infrastructure.rules.exception.ErrorType;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization.MembershipDetails;
import java.util.Optional;

public class TestProjectExtractor {

  public static MembershipDetails extractProjectDetails(ReportPortalUser user, String projectKey) {
    return Optional.ofNullable(rpUserToMembership(user))
        .orElseThrow(() -> new ReportPortalException(ErrorType.ACCESS_DENIED,
            "Please check the list of your available projects."
        ));
  }

}
