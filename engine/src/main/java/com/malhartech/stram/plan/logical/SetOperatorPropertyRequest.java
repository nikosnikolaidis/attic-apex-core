/*
 *  Copyright (c) 2012-2013 Malhar, Inc.
 *  All Rights Reserved.
 */
package com.malhartech.stram.plan.logical;

import com.malhartech.stram.plan.physical.PlanModifier;

/**
 *
 * @author David Yan <davidyan@malhar-inc.com>
 */
public class SetOperatorPropertyRequest extends LogicalPlanRequest
{
  private String operatorName;
  private String propertyName;
  private String propertyValue;

  public String getOperatorName()
  {
    return operatorName;
  }

  public void setOperatorName(String operatorName)
  {
    this.operatorName = operatorName;
  }

  public String getPropertyName()
  {
    return propertyName;
  }

  public void setPropertyName(String propertyName)
  {
    this.propertyName = propertyName;
  }

  public String getPropertyValue()
  {
    return propertyValue;
  }

  public void setPropertyValue(String propertyValue)
  {
    this.propertyValue = propertyValue;
  }

  @Override
  public void execute(PlanModifier pm)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
