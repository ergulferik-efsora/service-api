/*
 * Copyright 2025 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.reportportal.serviceapi.infrastructure.persistence.entity.tms;

import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.item.TestItem;
import com.epam.reportportal.serviceapi.infrastructure.persistence.entity.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tms_test_folder", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class TmsTestFolder implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false, precision = 64)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "project_id", nullable = false)
  private Project project;

  @OneToMany(mappedBy = "testFolder")
  private List<TmsTestCase> testCases;

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private TmsTestFolder parentTestFolder;

  @OneToMany(mappedBy = "parentTestFolder")
  private List<TmsTestFolder> subFolders;

  @ManyToMany
  @JoinTable(
      name = "tms_test_folder_test_item",
      joinColumns = @JoinColumn(name = "test_folder_id"),
      inverseJoinColumns = @JoinColumn(name = "test_item_id"))
  @ToString.Exclude
  private Set<TestItem> testItems;
}
