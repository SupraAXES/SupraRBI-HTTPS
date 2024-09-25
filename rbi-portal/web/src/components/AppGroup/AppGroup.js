import React, { useState } from 'react';
import { Row, Col } from 'antd';
import { Link } from 'react-router-dom';
import { trucateInBegin, trucateInMiddle } from '../../common/util.js';

import './AppGroup.css';

let AppGroup = (props) => {
  const { title, items } = props;

  return (
    <>
    <div className='group-container'>
      <Row className='group-title-container'>
        <Col className='group-title-item'>
          <div className='group-title-icon' />
        </Col>
        <Col className='group-title-item'>
          <span className='group-title'>{title}</span>
        </Col>
      </Row>
      <Row className='group-item-container' gutter={[50, 30]} justify='start'>
        {items.map((item, index) => {
          const iconEl = item.icon ? (
            <img src={item.icon} alt='' className='app-image' />
            ) : (
              <div className='app-default-icon'>{trucateInBegin(item.name, 4)}</div>
            );
          const titleEl = <div className='app-name'>{trucateInMiddle(item.name, 8, 8)}</div>;
          return (
          <Col className='item-container' key={index}>
            <Link className={'app-button'} target='_blank' to={`/temp?id=${item.id}`}>
              {iconEl}
            </Link>
            {titleEl}
          </Col>)
        })
        }
      </Row>
    </div>
    </>
  );
};

export default AppGroup;