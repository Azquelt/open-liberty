/*******************************************************************************
 * Copyright (c) 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.ws.ormdiag.example.war;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.ws.ormdiag.example.ejb.ExampleEJBService;
import com.ibm.ws.ormdiag.example.jpa.ExampleEntity;

/**
 * A servlet which uses JPA to persist guest data.
 */
@WebServlet(urlPatterns = "/ExampleServlet")
public class ExampleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private ExampleEJBService mas = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve a list of all the JPAEntities currently persisted
            List<ExampleEntity> entities = new ArrayList<ExampleEntity>();
            mas.retrieveAllEntities().map(e -> e).forEach(m -> entities.add(m));

            request.setAttribute("entities", entities);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            response.getWriter().println("Something went wrong. Caught exception " + e);
            response.getWriter().flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String str1 = request.getParameter("str1");
            String str2 = request.getParameter("str2");
            String str3 = request.getParameter("str3");
            if (str1 != null && str2 != null && str3 != null
                && str1.length() > 0 && str2.length() > 0 && str3.length() > 0) {

                // Create a new entity based on the incoming content
                ExampleEntity entity = new ExampleEntity();
                entity.setStr1(str1);
                entity.setStr2(str2);
                entity.setStr3(str3);
                mas.addEntity(entity);
            }
        } catch (Exception e) {
            response.getWriter().println("Something went wrong. Caught exception " + e);
            response.getWriter().flush();
        }

        doGet(request, response);
    }
}
