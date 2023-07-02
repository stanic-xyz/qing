/*
 * Copyright (c) 2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.app.web.content;

import java.util.List;

public class Project {

    private String slug;

    private String name;

    private String repositoryUrl;

    private ProjectStatus status;

    private List<Release> releases;

    public Project() {
    }

    public Project(String slug, String name, String repositoryUrl, ProjectStatus status) {
        this.slug = slug;
        this.name = name;
        this.repositoryUrl = repositoryUrl;
        this.status = status;
    }

    public String getSlug() {
        return this.slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepositoryUrl() {
        return this.repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public ProjectStatus getStatus() {
        return this.status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<Release> getReleases() {
        return this.releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

}
