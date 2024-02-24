import { listGeneratorVoByPageUsingPost,listGeneratorVoByPageFastUsingPost } from '@/services/backend/generatorController';
import { doGeneratorFavourUsingPost } from '@/services/backend/generatorFavourController';
import { doThumbUsingPost } from '@/services/backend/generatorThumbController';
import {
  UserOutlined,
  DownOutlined,
  LikeFilled,
  LikeOutlined,
  StarFilled,
  StarOutlined,
  UpOutlined,
} from '@ant-design/icons';
import {
  PageContainer,
  ProFormSelect,
  ProFormText,
  ProList,
  QueryFilter,
} from '@ant-design/pro-components';
import {Alert, Flex, Image, Input, message, Tabs, Tag ,Avatar,Typography,List,Card} from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'umi';
import moment from 'moment';
import Marquee from 'react-fast-marquee';

/**
 * 默认分页参数 ts语法
 */
const DEFAULT_PAGE_PARAMS: PageRequest = {
  current: 1,
  pageSize: 4,
  sortField: 'createTime',
  sortOrder: 'descend',
};

/**
 * 主页
 * @constructor
 */
const IndexPage: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  // 接受数据
  const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
  // 数据总数
  const [total, setTotal] = useState<number>(0);
  // 搜索条件
  const [searchParams, setSearchParams] = useState<API.GeneratorQueryRequest>({
    ...DEFAULT_PAGE_PARAMS,
  });
  const [showFilter, setShowFilter] = useState<boolean>(false);

  /**
   * 搜索
   */
  const doSearch = async () => {
    setLoading(true);
    try {
      const res = await listGeneratorVoByPageUsingPost(searchParams);
      setDataList(res.data?.records ?? []);
      setTotal(Number(res.data?.total) ?? 0);
    } catch (error: any) {
      message.error('获取数据失败，' + error.message);
    }
    setLoading(false);
  };

  useEffect(() => {
    doSearch();
    console.log(dataList)
  }, [searchParams]);

  const IconText = ({ icon, text, onClick }: { icon: any; text: string; onClick?: () => void }) => (
    <span onClick={onClick}>
      {React.createElement(icon, { style: { marginInlineEnd: 8 } })}
      {text}
    </span>
  );

  const doThumb = async (req: API.GeneratorThumbAddRequest) => {
    setLoading(true);
    try {
      const res = await doThumbUsingPost(req);
      if (res.code === 0) {
        message.success(res.data === 1 ? '点赞成功！' : '取消点赞！');
        setSearchParams({
          ...searchParams,
        });
      }
    } catch (error: any) {
      message.error('失败！', error.message);
    }
    setTimeout(() => {
      message.destroy(); // 关闭所有消息提示
    }, 3000);
    setLoading(false);
  };

  const doFavour = async (req: API.GeneratorFavourAddRequest) => {
    setLoading(true);
    try {
      const res = await doGeneratorFavourUsingPost(req);
      if (res.code === 0) {
        message.success(res.data === 1 ? '收藏成功！' : '取消收藏！');
        setSearchParams({
          ...searchParams,
        });
      }
    } catch (error: any) {
      message.error('失败！', error.message);
    }
    setTimeout(() => {
      message.destroy(); // 关闭所有消息提示
    }, 3000);
    setLoading(false);
  };
  /**
   * 标签列表
   * @param tags
   */
  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }

    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag) => (
          <Tag key={tag}>{tag}</Tag>
        ))}
      </div>
    );
  };

  return (
    // 用于包裹页面内容，提供一些页面级别的配置和功能，比如页面标题、面包屑导航等。
    <PageContainer title={<></>}>
      <Alert
        banner
        type="success"
        message={
          <Marquee pauseOnHover gradient={false}>
            欢迎各位使用 EasyCode代码生成平台，自动生成常见、重复性的代码片段，提高开发效率！！！
          </Marquee>
        }
      />
      {/* 布局盒子justify="center" 属性表示设置子元素在主轴（水平方向）上居中对齐。
          具体来说，justify 属性用于控制子元素在主轴上的排列方式 */}
      <Flex justify="center">
        {/* 输入搜索框 */}
        <Input.Search
          style={{
            width: '40vw',
            minWidth: 320,
          }}
          placeholder="请输入想要查找的生成器"
          allowClear
          enterButton="搜索"
          size="large"
          onChange={(e) => {
            searchParams.searchText = e.target.value;
          }}
          onSearch={(value: string) => {
            // 保证搜索之后，回到第一页
            setSearchParams({
              ...searchParams,
              ...DEFAULT_PAGE_PARAMS,
              searchText: value,
            });
          }}
        />
      </Flex>
      <div style={{ marginBottom: 16 }} />
      <Tabs
        defaultActiveKey="newest"
        onChange={(e) => {
          if (e === 'newest') {           
            setSearchParams({             
              ...searchParams,
              ...DEFAULT_PAGE_PARAMS,
              sortOrder: e,
              sortField: 'createTime',
            });
          } else {
            setSearchParams({             
              ...searchParams,
              ...DEFAULT_PAGE_PARAMS,
              sortOrder: e,
              sortField: 'thumbNum',
            });
          }
        }}
        tabBarExtraContent={
          <a
            style={{
              display: 'flex',
              gap: 4,
            }}
            onClick={() => {
              setShowFilter(!showFilter);
            }}
          >
            高级筛选 {showFilter ? <UpOutlined /> : <DownOutlined />}
          </a>
        }
        items={[
          {
            key: 'newest',
            label: '最新',
          },
          {
            key: 'recommend',
            label: '推荐',
          },
        ]}
      />{' '}
      {showFilter ? (
        <QueryFilter
          span={12}
          labelWidth="auto"
          split
          onFinish={async (values: API.GeneratorQueryRequest) => {
            setSearchParams({
              ...DEFAULT_PAGE_PARAMS,
              searchText: searchParams.searchText,
              ...values,
            });
          }}
        >
          <ProFormSelect label="标签" name="tags" mode="tags" />
          <ProFormText label="名称" name="name" />
          <ProFormText label="描述" name="description" />
        </QueryFilter>
      ) : null}
      
      <div style={{ marginBottom: 12 }} />
      <List<API.GeneratorVO>
        rowKey="id"
        loading={loading}
        grid={{
          gutter: 16,
          xs: 1,
          sm: 2,
          md: 3,
          lg: 3,
          xl: 4,
          xxl: 4,
        }}
        dataSource={dataList}
        pagination={{
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total,
          onChange(current: number, pageSize: number) {
            setSearchParams({
              ...searchParams,
              current,
              pageSize,
            });
          },
        }}
        renderItem={(data) => (
          <List.Item>
            <Link to={`/generator/detail/${data.id}`}>
              <Card hoverable cover={<Image alt={data.name} src={data.picture} />}>
                <Card.Meta
                  title={<a>{data.name}</a>}
                  description={
                    <Typography.Paragraph ellipsis={{ rows: 2 }} style={{ height: 44 }}>
                      {data.description}
                    </Typography.Paragraph>
                  }
                />
                {tagListView(data.tags)}
                <Flex justify="space-between" align="center">
                <IconText
                  icon={data.hasThumb ? LikeFilled : LikeOutlined}
                  // @ts-ignore
                  text={data.thumbNum}
                  key="list-vertical-like-o"
                  onClick={(e) => {
                    e.preventDefault(); 
                    doThumb({
                      generatorId: data.id,
                    });
                    console.log(data.thumbNum);
                  }}
                />
                <IconText
                  icon={data.hasFavour ? StarFilled : StarOutlined}
                  // @ts-ignore
                  text={data.favourNum}
                  key="list-vertical-star-o"
                  onClick={(e) => {
                    e.preventDefault(); 
                    doFavour({
                      generatorId: data.id,
                    });
                  }}
                />
                  <Typography.Text type="secondary" style={{ fontSize: 12 }}>
                    {moment(data.createTime).fromNow()}
                  </Typography.Text>
                  <div>
                    <Avatar src={data.user?.userAvatar ?? <UserOutlined />} />
                  </div>
                </Flex>
              </Card>
            </Link>
          </List.Item>
        )}
      />
    </PageContainer>
  );
};

export default IndexPage;
