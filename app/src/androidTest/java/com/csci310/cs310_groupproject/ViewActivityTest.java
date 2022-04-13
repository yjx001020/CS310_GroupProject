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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import java.sql.Connection;

@RunWith(MockitoJUnitRunner.class)
public class ViewActivityTest{
    @InjectMocks
    private ViewActivity view;
    @Mock
    Connection conn;
    @Mock
    PreparedStatement pst;
    @Mock
    ResultSet result;
    @Before
    public void initTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testInfoAsyncTask() throws Exception {
        Mockito.when(conn.createStatement()).thenReturn(pst);
        Mockito.when(conn.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
    }

}