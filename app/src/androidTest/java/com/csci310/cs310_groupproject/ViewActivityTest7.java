package com.csci310.cs310_groupproject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ViewActivityTest7 {

    @Mock
    Connection conn;

    @Mock
    PreparedStatement pst;

    @Mock
    ResultSet result;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void cleanup() throws Exception {
        closeable.close();
    }

    @Test
    public void testCreateDateNoEvent() throws Exception {
        doReturn(pst).when(conn).prepareStatement(any());
        String[] invitees = new String[] { "a", "b" };
        ViewActivity view = new ViewActivity();

        String[] events = new String[] {};
        try {
            view.Callgetmode(conn, "4");
            fail("Expected throw");
        } catch (Exception e) {
            fail("Unexpected exception type");
        }
    }
}
