package com.epam.reportportal.serviceapi.reporting.async.producer;

import static com.epam.reportportal.ReportPortalUserUtil.getRpUser;
import static com.epam.reportportal.serviceapi.infrastructure.persistence.util.MembershipUtils.rpUserToMembership;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.epam.reportportal.serviceapi.infrastructure.persistence.commons.ReportPortalUser;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.organization.OrganizationRole;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.project.ProjectRole;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.user.UserRole;
import com.epam.reportportal.serviceapi.reporting.StartLaunchRQ;
import com.epam.reportportal.serviceapi.reporting.async.producer.LaunchStartProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;

@ExtendWith(MockitoExtension.class)

class LaunchStartProducerTest {

  @Mock
  AmqpTemplate amqpTemplate;

  @InjectMocks
  LaunchStartProducer launchStartProducer;

  @Test
  void starLaunch() {
    StartLaunchRQ request = new StartLaunchRQ();
    ReportPortalUser user = getRpUser("test", UserRole.ADMINISTRATOR, OrganizationRole.MANAGER,
        ProjectRole.EDITOR, 1L);

    launchStartProducer.startLaunch(user, rpUserToMembership(user), request);
    verify(amqpTemplate).convertAndSend(any(), any(), any(), any());
  }

}
